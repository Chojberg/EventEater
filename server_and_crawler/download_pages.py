from bs4 import BeautifulSoup
from urllib.request import urlopen
import time
import os
import sys

# Pages
pgs_filename = 'pages_to_download.txt'
download_directory = 'events'

def download_and_save(page, filename):
	request = urlopen(page)
	path = os.path.join(download_directory,filename)
	f = open('{}.txt'.format(path), 'w')
	raw_html = str(request.read())
	request.close()

	soup = BeautifulSoup(raw_html, 'html.parser')
	res = soup('script',{'type': 'application/ld+json'})
	for event in res:
		try :
			d = eval(event.contents[0].replace('null', 'None').strip())
		except:
			continue;
		print(d.keys())
		print("Event: {}".format(d['name']))
		print("Location: {}".format(d['location']))
		print("URL: {}".format(d['url']))
		print("Start date: {}".format(d['startDate']))
		print("End date: {}".format(d['endDate']))

		# Get the description from the url
		print("Opening {}...".format(d['url']))
		req = urlopen(d['url'])
		raw = str(req.read())
		req.close()
		soup = BeautifulSoup(raw, 'html.parser')
		res = soup.findAll('div', {'class':'description'})
		#print(res)
		print("==")
		if res != []:
			for desc in res:
				print(desc.contents)
				print(type(desc.contents))
				print(len(desc.contents))
				print(desc.contents[0])
				print(type(desc.contents[0]))
				d['description'] = str(desc.contents[0]).replace('null','None')
		else:
			d['description'] = ''
		f.write(str(d) + '\n')
		print("================================")
		time.sleep(5)

	f.close()

def main() -> None:
	pages_to_dl = []

	with open(pgs_filename, 'r') as f:
		for line in f.readlines():
			splt = line.strip().split()
			pages_to_dl.append((splt[0],splt[1]))

	for page, title in pages_to_dl:
		print("Downloading {}...".format(page))
		download_and_save(page,title)
		print("Done! sleeping...")
		time.sleep(10)

if __name__ == '__main__':
	main()
