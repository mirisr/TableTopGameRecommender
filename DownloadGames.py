# coding: utf-8
import requests
from bs4 import BeautifulSoup
import re
import json
import urllib
import urllib2
import csv
import sys
from HTMLParser import HTMLParser

class MLStripper(HTMLParser):
    def __init__(self):
        self.reset()
        self.fed = []
    def handle_data(self, d):
        self.fed.append(d)
    def get_data(self):
        return ''.join(self.fed)

def strip_tags(html):
	if html == None:
		return "-1"
	s = MLStripper()
	s.feed(html)
	return s.get_data()



#user_agent = 'Safari'
#headers = { 'User-Agent' : user_agent }
headers = { 'User-Agent': 'Mozilla/5.0 (Windows NT 6.0; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0' }


##globals
CATEGORIES = { "all" : "",
              "abstracts": "abstracts/",
              "customizable": "cgs/",
              "childrens": "childrensgames/",
              "family": "familygames/",
              "party": "partygames/",
              "strategy": "strategygames/",
              "thematic": "thematic/",
              "war": "wargames/"}

BASE_URL = "http://boardgamegeek.com/{0}browse/boardgame/page/{1}"

NUM_PATTERN = re.compile("\d+")

gameFile = open("Game_Info_More_14.csv", 'wt')
writer = csv.writer(gameFile)
'''
entry_dict["types"],
        entry_dict["mechanisms"], entry_dict["description"], entry_dict["image"])'''

writer.writerow(('id', 'title', 'year', 'avg rating', 'no. of ratings', 
    'avg complexity', 'min players', 'max players', 'min play time',
    'max play time', 'categories', 'types', 'mechanisms', 'description', 'image'))
def get_page_text(page_num, category):
    """
        For now this function is here for ease of unit testing
    """
    page = requests.get(BASE_URL.format(CATEGORIES[category], str(page_num + 1)))
    return page.text

'''
<average value="8.63884"></average>
<ranks>
<rank bayesaverage="8.4085" friendlyname="Board Game Rank" id="1" name="boardgame" type="subtype" value="1"></rank>
<rank bayesaverage="8.43622" friendlyname="Thematic Rank" id="5496" name="thematic" type="family" value="1"></rank>
<rank bayesaverage="8.41575" friendlyname="Strategy Game Rank" id="5497" name="strategygames" type="family" value="1"></rank>
</ranks>

<owned value="18096"></owned>

<averageweight value="2.8085"></averageweight>

'''
def get_features(link= "", entry_dict = {}):
   
    link += "/ratings"
    req = requests.get(link, headers=headers)
    
    req.connection.close()
    soup = BeautifulSoup(req.text)
    
    script = soup.find('script')
    if not (script == None):
	    script = script.string
	    
	    m = re.compile('GEEK.geekitemPreload = (.*);').search(script) 
	    if not (m == None):
	        stuff = m.group(0)

	        stuff = stuff.replace("GEEK.geekitemPreload =", "\"GEEK.geekitemPreload\" :")
	        stuff = "{" + stuff[:-1] + "}"

	        info = json.loads(stuff)

	        details = info['GEEK.geekitemPreload']['item']
	        stats = details['stats']

	        # POPULARITY
	        num_user_ratings = stats['usersrated']
	        avg_user_ratings = stats['average']

	        print "No. User Ratings:",num_user_ratings
	        print "Avg. User Ratings:", avg_user_ratings

	        entry_dict["no_ratings"] = num_user_ratings
	        entry_dict["avg_rating"] = avg_user_ratings

	        # COMPLEXITY
	        avg_weight = stats['avgweight']

	        print "Avg. Weight:", avg_weight

	        entry_dict["avg_weight"] = avg_weight

	        # PLAYING TIME
	        min_play_time = details['minplaytime']
	        max_play_time = details['maxplaytime']

	        print "Min Play Time:", min_play_time
	        print "Max Play Time:", max_play_time

	        entry_dict["min_play_time"] = min_play_time
	        entry_dict["max_play_time"] = max_play_time

	        # NO. OF PLAYERS
	        min_players = details['minplayers']
	        max_players = details['maxplayers']

	        print "Min Players:", min_players
	        print "Max Players:", max_players

	        entry_dict["min_players"] = min_players
	        entry_dict["max_players"] = max_players

	        # CATEGORIES (i.e. subdomains on BGG.com)
	        categories_str = ""
	        categories = details['links']['boardgamesubdomain']
	        for category in categories:
	            categories_str += category['name'].replace(" Games", "") + " "

	        categories_str = categories_str[:-1]
	        #print "Categories:", categories_str

	        entry_dict["categories"] = categories_str

			# DESCRIPTION
	        description = strip_tags(details['description'])
	        #print "Description:", description
	        entry_dict["description"] = description.encode('utf-8')

	        # IMAGE
	        if 'images' not in details:
	        	entry_dict["image"] = -1
	        else:
		    	image = details['images']['thumb']
		        image = "http:"+strip_tags(image)
		        entry_dict["image"] = image
	        #print "Image:", image

	        # TYPES (i.e. categories on BGG.com)
	        types_str=""
	        types_json = details['links']['boardgamecategory']
	        for t in types_json:
	        	types_str += (t['name'] + " ")

	        entry_dict["types"] = types_str
	        #print "Types:", types_str

	        # MECHANISMS 
	        mechanisms_str = ""
	        mechanisms_json = details['links']['boardgamemechanic']
	        for m in mechanisms_json:
	        	mechanisms_str += (m['name'])
	        entry_dict["mechanisms"] = mechanisms_str
	        #print "Mechanisms:", mechanisms_str
	        return entry_dict

	    
    entry_dict["no_ratings"] = -1
    entry_dict["avg_rating"] = -1
    entry_dict["avg_weight"] = -1
    entry_dict["min_play_time"] = -1
    entry_dict["max_play_time"] = -1
    entry_dict["min_players"] = -1
    entry_dict["max_players"] = -1
    entry_dict["categories"] = -1
    entry_dict["description"] = -1
    entry_dict["image"] = -1
    entry_dict["types"] = -1
    entry_dict["mechanisms"] = -1

    return entry_dict

def extract_game_details_from_entry(tag):
    """
    This function will extract the pertinent game information out of the
    beautiful soup "tag" of filtered data from the web page
    :param: tag - beautiful soup HTML entry that has the game data
    :return: a dict of game title, game id, year publish
    """
    #get contents of tag
    contents = tag.contents
    #strip "returns"
    contents = [x for x in contents if x != u'\n']
    
    #get link
    link = str(contents[0])
    link = "http://boardgamegeek.com"+re.findall(r'"([^"]*)"',link)[0]
    
    entry_dict = {}
    # get id
    match = NUM_PATTERN.search(contents[0].attrs['href'])
    
    entry_dict["gameid"] = match.group(0)
    
    # get game title
    entry_dict["title"] = contents[0].text.encode('utf-8')
    print "Title:", entry_dict["title"]
    # get year
    try:
        match = NUM_PATTERN.search(contents[1].text)
    except:
        entry_dict["year"] = "N/A"
        return entry_dict

    try:
        entry_dict["year"] = match.group(0)
    except AttributeError:
        entry_dict["year"] = "N/A"
        
       
    entry_dict = get_features(link, entry_dict)
    
    #print entry_dict

    #'id', 'title', 'year', 'avg rating', 'no. of ratings', 
    #'avg complexity' 'min players', 'max players', 'min play time', 
    #'max play time', 'categories'
    row = (entry_dict["gameid"], entry_dict["title"], entry_dict["year"], 
        entry_dict["avg_rating"], entry_dict["no_ratings"], entry_dict["avg_weight"], 
        entry_dict["min_players"], entry_dict["max_players"],entry_dict["min_play_time"], 
        entry_dict["max_play_time"], entry_dict["categories"], entry_dict["types"],
        entry_dict["mechanisms"], entry_dict["description"], entry_dict["image"])
    
    writer.writerow(row)

    return entry_dict

def extract_games_from_page(page_num, category, stop_at=100):
    """
    This function will retrieve the BGG game page and pull out all the games 
    on the page.  If stop_at is not 100, it will cut the list short, since the
    page always has 100 games on it.
    :param: page_num - the page number
    :param: category - what category of game is it
    :param: stop_at - the number to stop at before 100
    :return: a list of games with their gameid in rank order
    """ 
    page = get_page_text(page_num, category)

    soup_parse = BeautifulSoup(page)

    #find the games 
    rough_cut = soup_parse.find_all('div', attrs={"style":"z-index:1000;"})
    game_list = []
    for index, rough_entry in enumerate(rough_cut):
        game_list.append(extract_game_details_from_entry(rough_entry))
        if index + 1 == stop_at:
            break

    return game_list

def get_list_of_top_games(number_of_games, category="all"):
    """
    This function will retrieve the top number of games based on the current
    BGG rankings.
    :param: number_of_games = how many games to retrieve
    :return: a list of games with their gameid in rank order
    """

    #determine the number of pages to retrieve
    number_of_pages = number_of_games / 100
    extra_games = number_of_games % 100


    games = []
    for page in xrange(number_of_pages):
        games.extend(extract_games_from_page(page, category))

    #grab extra
    if extra_games != 0 and number_of_pages != 0:
        games.extend(extract_games_from_page(number_of_pages + 1, category, stop_at=extra_games))
    elif extra_games != 0 and number_of_pages == 0:
        games.extend(extract_games_from_page(number_of_pages, category, stop_at=extra_games))
    
    return games


# there are 868 pages

for page in xrange(866,868):
	print "___________________________________________Page: ", page
	games = extract_games_from_page(page, "all")

gameFile.close()   

#games = get_list_of_top_games(100)
#print games