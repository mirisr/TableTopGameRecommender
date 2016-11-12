import csv
import sys
import MySQLdb
import os.path


#connect
db = MySQLdb.connect(host="localhost", user="root", passwd="103191",
db="BoardGames")

# create a database cursor
cursor = db.cursor()

CategoryNames = ["Acting","Action / Movement Programming","Action Point Allowance System","Area Control / Area Influence","Area Enclosure","Area Movement","Area-Impulse",
"Auction/Bidding","Betting/Wagering","Campaign / Battle Card Driven","Card Drafting","Chit-Pull System",
"Co-operative Play","Commodity Speculation","Crayon Rail System","Deck / Pool Building","Dice Rolling",
"Grid Movement","Hand Management","Hex-and-Counter","Line Drawing","Memory","Modular Board","Paper-and-Pencil",
"Partnerships","Pattern Building","Pattern Recognition","Pick-up and Deliver","Player Elimination",
"Point to Point Movement","Press Your Luck","Rock-Paper-Scissors","Role Playing","Roll / Spin and Move",
"Route/Network Building","Secret Unit Deployment","Set Collection","Simulation","Simultaneous Action Selection",
"Singing","Stock Holding","Storytelling","Take That","Tile Placement","Time Track","Trading",
"Trick-taking","Variable Phase Order","Variable Player Powers","Voting","Worker Placement"]

print len(CategoryNames)

# Creates a list containing 5 lists, each of 8 items, all set to 0
count = 0
w, h = 51, 51 
Frequencies = [[0 for x in range(w)] for y in range(h)] 
CoOccurance = [[0 for x in range(w)] for y in range(h)] 


def CleanCOFToReImport():
	query = "delete from MCOF"
	cursor.execute(query)
	db.commit()


def UpdateCoOccuranceFrequencies(data, count):
	#['Wargames']
	#['-1']
	#['']
	indexes = []
	gameId = int(data[0])
	categories = data[12].split(":")
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

			query = """ INSERT INTO MCOF VALUES (%s, %s, %s)"""
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

print "No. Of Games with Mechanics:", count
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