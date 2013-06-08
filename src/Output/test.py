'''
Demo to show use of the engineering Formatter.
'''

import matplotlib.pyplot as plt
import numpy as np

from matplotlib.ticker import EngFormatter

with open('./out.txt', 'r') as f:
	data = f.read()

with open('./out2.txt', 'r') as f:
	data2 = f.read()
    
with open('./out3.txt', 'r') as f:
	data3 = f.read()
	
    
split_distance = data3.split("\n")

dic = {}

for element in split_distance:
    content = element.split(";")
    if len(content) == 4:
        clazz = content[2]
        x = content[0]
        y = content[1]
        reference = content[3]
        if clazz in dic:
            if reference in dic[clazz]:
                dic[clazz][reference][0].append(x)
                dic[clazz][reference][1].append(y)  
            else:
                dic[clazz][reference] = {0 : [], 1 : []}
        else:
            dic[clazz] = {}
            if reference in dic[clazz]:
                dic[clazz][reference][0].append(x)
                dic[clazz][reference][1].append(y)  
            else:
                dic[clazz][reference] = {0 : [], 1 : []}
    



split1 = data.split('\n')
splitx = data2.split('\n')

daten = []
boolean = []
average = []

average_neg = []
average_neg_x = []

date_neg_x = []
date_neg = []
counter = 0

for i in split1:
	counter += 1
	split2 = i.split(';')
	if(len(split2) == 3):
		daten.append(split2[0])
		if split2[1] == 'true':
			boolean.append(600)
		else:
			boolean.append(-600)
		average.append(split2[2])
		if float(split2[0]) < 0:
			date_neg.append(float(split2[0]))
			date_neg_x.append(counter)
		if float(split2[2]) < 0:
			average_neg.append(float(split2[2]))
			average_neg_x.append(counter)
			

x = range(len(split1)-1)

ax = plt.subplot(111)
ax.plot([i*8 for i in x], daten, 'r-')
ax.plot([i*8 for i in x], boolean, 'g-')
ax.plot([i*8 for i in x], average, 'b-')
#ax.plot(date_neg_x, date_neg, 'm+')
ax.plot(average_neg_x, average_neg, 'yo')

for j in splitx:
	print 'huhu'
	splitx2 = j.split(';')
	if(len(splitx2) == 2):
		print 'moep'
		blub = range(int(splitx2[0]), int(splitx2[1]))
		ax.plot(blub, [-2000 for i in blub], 'k-', linewidth=10.0)


m = {
 "data0.wav" : "Distance",
 "data1.wav" : "Meter",
 "data2.wav" : "Point",
 "data3.wav" : "0",
 "data4.wav" : "1",
 "data5.wav" : "2",
 "data6.wav" : "3",
 "data7.wav" : "4",
 "data8.wav" : "5",
 "data9.wav" : "6",
 "data10.wav" : "7",
 "data11.wav" : "8",
 "data12.wav" : "9",
 "data13.wav" : "Distance",
 "data14.wav" : "Meter",
 "data15.wav" : "Point",
 "data16.wav" : "0",
 "data17.wav" : "1",
 "data18.wav" : "2",
 "data19.wav" : "3",
 "data20.wav" : "4",
 "data21.wav" : "5",
 "data22.wav" : "6",
 "data23.wav" : "7",
 "data24.wav" : "8",
 "data25.wav" : "9",
 "data26.wav" : "5",
 "data27.wav" : "3",
 "data28.wav" : "4",
 "data29.wav" : "8",
}
plt.show()

fig = plt.figure()
ax2 = fig.add_subplot(111) 
i = 0
colors = "bgcmykbgcrmykbgcmykbgcrmykgcmykbgcrmyk"
position = "2"
for ref in dic[position]:
    x = dic[position][ref][0]
    y = dic[position][ref][1]
    ax2.plot(x, y, marker="o", linestyle='.',  label=m[ref], markeredgecolor=colors[i], color=colors[i])
    ax2.text(float(x[len(x)-1])+5, float(y[len(y)-1]), str(m[ref]))
    i+=1

handles, labels = ax2.get_legend_handles_labels()

# reverse the order
ax2.legend(handles[::-1], labels[::-1])

# or sort them by labels
import operator
hl = sorted(zip(handles, labels),
            key=operator.itemgetter(1))
handles2, labels2 = zip(*hl)

ax2.legend(handles2, labels2)

plt.show()

