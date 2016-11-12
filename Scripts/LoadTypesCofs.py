import csv
import sys
import MySQLdb
import os.path


#connect
db = MySQLdb.connect(host="localhost", user="root", passwd="103191",
db="BoardGames")

# create a database cursor
cursor = db.cursor()

CategoryNames = [
"Abstract Strategy",
"Action / Dexterity",
"Adventure",
"Age of Reason",
"American Civil War",
"American Indian Wars",
"American Revolutionary War",
"American West",
"Ancient",
"Animals",
"Arabian",
"Aviation / Flight",
"Bluffing",
"Book",
"Card Game",
"Children's Game",
"City Building",
"Civil War",
"Civilization",
"Collectible Components",
"Comic Book / Strip",
"Deduction",
"Dice",
"Economic",
"Educational",
"Electronic",
"Environmental",
"Expansion for Base-game",
"Exploration",
"Fan Expansion",
"Fantasy",
"Farming",
"Fighting",
"Game System",
"Horror",
"Humor",
"Industry / Manufacturing",
"Korean War",
"Mafia",
"Math",
"Mature / Adult",
"Maze",
"Medical",
"Medieval",
"Memory",
"Miniatures",
"Modern Warfare",
"Movies / TV / Radio theme",
"Murder/Mystery",
"Music",
"Mythology",
"Napoleonic",
"Nautical",
"Negotiation",
"Novel-based",
"Number",
"Party Game",
"Pike and Shot",
"Pirates",
"Political",
"Post-Napoleonic",
"Prehistoric",
"Print & Play",
"Puzzle",
"Racing",
"Real-time",
"Religious",
"Renaissance",
"Science Fiction",
"Space Exploration",
"Spies/Secret Agents",
"Sports",
"Territory Building",
"Trains",
"Transportation",
"Travel",
"Trivia",
"Video Game Theme",
"Vietnam War",
"Wargame",
"Word Game",
"World War I",
"World War II",
"Zombies"]

print len(CategoryNames)

# Creates a list containing 5 lists, each of 8 items, all set to 0
count = 0
w, h = 84, 84 
Frequencies = [[0 for x in range(w)] for y in range(h)] 
CoOccurance = [[0 for x in range(w)] for y in range(h)] 


def CleanCOFToReImport():
	query = "delete from TCOF"
	cursor.execute(query)
	db.commit()


def UpdateCoOccuranceFrequencies(data, count):
	#['Wargames']
	#['-1']
	#['']
	indexes = []
	gameId = int(data[0])
	categories = data[11].split(":")
	for category in categories:
		if not category == '-1' and not category == '':
			count+=1
			index = CategoryNames.index(category)
			#print category, index
			indexes.append(index)
			

	for row in indexes:
		for col in indexes:
			Frequencies[row][col] += 1

	return count


def NormalizeFrequencies():
	for row in xrange(len(Frequencies[0])):
		for col in xrange(len(Frequencies)):
			#print row, col
			CoOccurance[row][col] = (Frequencies[row][col]) / float(Frequencies[row][row] + Frequencies[col][col] - Frequencies[row][col])
			#round
			CoOccurance[row][col] = round(CoOccurance[row][col],2)

def ImportCoOccuranceFrequencies():
	for row in xrange(len(Frequencies[0])):
		for col in xrange(len(Frequencies)):
			category1 = CategoryNames[row]
			category2 = CategoryNames[col]
			similarity = CoOccurance[row][col]

			query = """ INSERT INTO TCOF VALUES (%s, %s, %s)"""
			data = (category1, category2, similarity)
			cursor.execute(query, data)
			db.commit()

CleanCOFToReImport()
filename = "RawData/Game_Info_More_Unique.csv"
line = 1
with open(filename, 'rb') as csvfile:
	reader = csv.reader(csvfile)
	for row in reader:
		if not row[0] == 'id':
			#print "...Importing", line
			count = UpdateCoOccuranceFrequencies(row, count)
			line += 1

print "No. Of Games with Types:", count
print "Matrix of Raw Frequencies"
NormalizeFrequencies()
for row in Frequencies:
	print row
print "-----------------"
print "Matrix of CoOccurence Similarities"
for row in CoOccurance:
	print row

ImportCoOccuranceFrequencies()

csvfile.close()

cursor.close()
db.close()