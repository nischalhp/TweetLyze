#compute tfidf
import math

class TfIdf:

	def computeTfIdf(self,entity_count_id_dict,total_doc_count):
		entity_tfidf_dict = {}
		tf_idf_list = []
		for key in entity_count_id_dict.keys():
			tf = entity_count_id_dict[key]
			print tf
			idf = math.log10(total_doc_count/tf)
			tfidf = tf * idf 
			tf_idf_list.extend((tf,idf,tfidf))
			entity_tfidf_dict[key] = tf_idf_list
			print entity_tfidf_dict[key]
			break






