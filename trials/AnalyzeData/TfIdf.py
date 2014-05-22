#compute tfidf
import math

class TfIdf:

	def computeTfIdf(self,entity_count_id_dict,total_doc_count):
		entity_tfidf_dict = {}
		tf_idf_list = []
		tf = 0
		idf = 0

		for key in entity_count_id_dict.keys():
			try:
				tf = entity_count_id_dict[key]
				idf = math.log10(total_doc_count/tf)
				tfidf = tf * idf 
				tf_idf_list.extend((tf,idf,tfidf))
				entity_tfidf_dict[key] = tf_idf_list
#				print entity_tfidf_dict[key]

			except:
				idf_1 = total_doc_count/tf
				print key,tf,idf,total_doc_count,math.log10(idf_1),math.log10(total_doc_count/tf)





