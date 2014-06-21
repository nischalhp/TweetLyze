from KMedoid import KMedoid

kmedoid_obj = KMedoid()
clusterid , trends_list = kmedoid_obj.generate_kmedoid('2295420')
print clusterid
count = 0
print trends_list
for cluster in clusterid:
	print trends_list[cluster],trends_list[count]
	count = count + 1

