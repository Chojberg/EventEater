import sys
import numpy as np
import sqlite3
import json
import operator
from bs4 import BeautifulSoup

class Retrieval():
	def __init__(self, debug=False):

		self._conn = sqlite3.connect('inverted_index.db')
		self._cursor = self._conn.cursor()
		self._N = 500

		if debug:
			while(True):
				response = str(input("Query: ")).lower()
				if response == 'exit':
					sys.exit()
				res = self.getResults(response)

	def query(self, query_string):
		return self.getResults(query_string)

	def getResults(self, query):
		word_list = query.split()

		tf_idf_results = {}
		for word in word_list:
			sql = "SELECT value FROM idx WHERE term='{}'".format(word)
			self._cursor.execute(sql)
			results = self._cursor.fetchall()
			if len(results) == 0:
				#print(" There were no results for {}!".format(word))
				return {}

			str_result = results[0][0]
			terms_with_freqs = str_result.split("|||")

			docIDs = []
			term_frequencies = []

			for tf_docID_string in terms_with_freqs:
				if tf_docID_string:
					split_thing = tf_docID_string.split("***")
					term_frequencies.append(float(split_thing[0]))
					docIDs.append(split_thing[1])
			df = len(docIDs)

			term_freqs = np.array(term_frequencies)
			tf_idf_values = term_freqs * np.log10(self._N/df)


			combined = zip(tf_idf_values, docIDs)
			for score, ID in combined:
				if ID in tf_idf_results:
					tf_idf_results[ID] += score
				else:
					tf_idf_results[ID] = score

		res = sorted(tf_idf_results.items(), key=operator.itemgetter(1), reverse= True)
		if len(res) >= 5:
			print("Showing top 5 results out of "+ str(len(res)) )
		else:
			print("Showing top "+ str(len(res)) +" results out of "+ str(len(res)))

		# res[i][0] is the event dict
		count = 5
		true_results = {}
		for i in range(len(res)):
			true_results['event{}'.format(i)] = eval(res[i][0])
			#self.printResult(res[i][0], res[i][1])
			count -= 1
			if count == 0:
				break
		return true_results

	def printResult(self, docID, score):
			print("Result:", eval(docID)['name'])
			print("Score :", score)











if __name__== "__main__":
	Retrieval()
