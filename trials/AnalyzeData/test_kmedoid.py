from KMedoid import KMedoid

kmedoid_obj = KMedoid()
return_matrix = kmedoid_obj.get_matrix('2295420')
for rows in return_matrix:
	print rows