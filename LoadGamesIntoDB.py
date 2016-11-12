import csv
import sys
import MySQLdb
import os.path


#connect
db = MySQLdb.connect(host="localhost", user="root", passwd="103191",
db="BoardGames")

# create a database cursor
cursor = db.cursor()
#delete from games

def CleanDBToReImport():
	query = "delete from games"
	cursor.execute(query)
	db.commit()

def ImportGameDetails(data):
	gameId = data[0]
	title = data[1]
	year = data[2]
	avgRating = data[3]
	noOfRatings = data[4]
	complexity = data[5]
	minPlayers = data[6]
	maxPlayers = data[7]
	minTime = data[8]
	maxTime = data[9]

	query = """ INSERT INTO Games VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""

	data = (int(gameId), title, int(year), float(avgRating), int(noOfRatings), float(complexity),
		int(minPlayers), int(maxPlayers), int(minTime), int(maxTime))
	cursor.execute(query, data)
	db.commit()

def ImportGameCategories(data):
	#['Wargames']
	#['-1']
	#['']
	gameId = int(data[0])
	categories = data[10].split(" ")
	for category in categories:
		if not category == '-1' and not category == '':
			query = """ INSERT INTO Categories VALUES (%s, %s)"""
			data = (gameId, category)
			cursor.execute(query, data)
			db.commit()

#'id', 'title', 'year', 'avg rating', 'no. of ratings', 
#'avg complexity' 'min players', 'max players', 'min play time', 
#'max play time', 'categories'

CleanDBToReImport()
filename = "Game_Info_70630.csv"
line = 1
with open(filename, 'rb') as csvfile:
	reader = csv.reader(csvfile)
	for row in reader:
		if not row[0] == 'id':
			print "...Importing", line
			ImportGameDetails(row)
			ImportGameCategories(row)
			line += 1

csvfile.close()

cursor.close()
db.close()