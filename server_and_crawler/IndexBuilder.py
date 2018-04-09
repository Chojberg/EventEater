import sqlite3
from bs4 import BeautifulSoup
import sys
import os
import re
downloaded_directory = "events"
from collections import defaultdict

class IndexBuilder():
	def __init__(self):
		self._conn = sqlite3.connect('inverted_index.db')
		self._cursor = self._conn.cursor()

		self._cursor.execute('CREATE TABLE IF NOT EXISTS idx (term TEXT primary key, value TEXT) ')
		self._conn.commit()

		for filename in os.listdir(downloaded_directory):
			file_to_read = os.path.join(downloaded_directory,filename)
			print("Reading file:",file_to_read)
			with open(file_to_read,'r',encoding='UTF-8', errors='ignore') as f:
				for line in f:
				# each event
					d = eval(line)
					self.collectTokens(d)
					self._conn.commit()


		self._conn.close()


	def collectTokens(self, event_dict):

		combined_str = event_dict['name'] + event_dict['description']
		print("Combined STR:", combined_str)
		freq_dict = defaultdict(int)
		for x in combined_str.split():
			x= x.lower()
			if x.isalnum():
				freq_dict[x] += 1
		print("FREQ_DICT:",freq_dict)
		print(event_dict)
		for key, value in event_dict.items():
			if type(value) == dict:
				for key2, value2 in value.items():
					if type(value2) == dict:
						for key3, value3 in value2.items():
							if value != None:
								value2[key3] = value3.replace("'", "")
					else:
						if value != None:
							value[key2] = value2.replace("'", "")
			else:
				if value != None:
					event_dict[key] = value.replace("'", "")

		for term, freq in freq_dict.items():
			self.updateDB(term, float(freq_dict[term]/sum(freq_dict.values())), str(event_dict))

	def updateDB(self, word, freq_normalized, event_dict_str):
		to_insert = '{}***{}|||'.format(freq_normalized,event_dict_str)

		sql = "SELECT value FROM idx WHERE term='{}'".format(word)
		self._cursor.execute(sql)

		results = self._cursor.fetchall()
		if len(results) != 0: #  Add more to to_insert
			# We have to concat it
			to_insert = results[0][0] + to_insert
			sql_update = 'UPDATE idx SET value = "{}" WHERE term = "{}"'.format(to_insert,word)
		else:
			sql_update = 'INSERT INTO idx (term, value) values ("{}", "{}")'.format(word,to_insert)
		print("SQL_UPDATE:",sql_update)
		self._cursor.execute(sql_update)








if __name__== "__main__":
	IndexBuilder()
