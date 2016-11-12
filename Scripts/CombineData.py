import csv

all = []

def isUnique(row):
	for r in all:
		if r[0] == row[0]:
			return False
	return True

mech = ["Acting","Action / Movement Programming","Action Point Allowance System","Area Control / Area Influence","Area Enclosure","Area Movement","Area-Impulse",
"Auction/Bidding","Betting/Wagering","Campaign / Battle Card Driven","Card Drafting","Chit-Pull System",
"Co-operative Play","Commodity Speculation","Crayon Rail System","Deck / Pool Building","Dice Rolling",
"Grid Movement","Hand Management","Hex-and-Counter","Line Drawing","Memory","Modular Board","Paper-and-Pencil",
"Partnerships","Pattern Building","Pattern Recognition","Pick-up and Deliver","Player Elimination",
"Point to Point Movement","Press Your Luck","Rock-Paper-Scissors","Role Playing","Roll / Spin and Move",
"Route/Network Building","Secret Unit Deployment","Set Collection","Simulation","Simultaneous Action Selection",
"Singing","Stock Holding","Storytelling","Take That","Tile Placement","Time Track","Trading",
"Trick-taking","Variable Phase Order","Variable Player Powers","Voting","Worker Placement"]

types = [
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


def getMechs(mechstr):
	mechsStr = ""
	for m in mech:
		if m in mechstr:
			if mechsStr == "":
				mechsStr += m
			else:
				mechsStr += ":" + m
	#print mechsStr
	return mechsStr

def getTypes(typestr):
	typesStr = ""
	for t in types:
		if t in typestr:
			if typesStr == "":
				typesStr += t
			else:
				typesStr += ":" + t
	#print typesStr
	return typesStr


for i in xrange(1, 15):
	readFile = "RawData/Game_Info_More_"+str(i)+".csv"
	print "Loading File " + str(i) + "..."
	with open(readFile, 'rb') as csvfile:
		reader = csv.reader(csvfile)
		for row in reader:
			if not row[0] == 'id':
				if isUnique(row):
					row[12] = getMechs(row[12])
					row[11] = getTypes(row[11])
					all.append(row)

	csvfile.close()



print "all", len(all)


gameFile = open("RawData/Game_Info_More_Unique.csv", 'wt')
writer = csv.writer(gameFile)

for r in all:
	writer.writerow(r)

gameFile.close()  



