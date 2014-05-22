#IR

##Boolean retrieval

    Incidence Matrix - Representation of terms in documents by a boolean value of 1 or 0
    EX:
           DOC1 DOC2 DOC3
    TERM1    0    1    1 
    TERM2    1    0    1
    TERM3    1    1    1
    
    Boolean Retrieval - Using and , or , not operations
  
    Adhoc Retrieval 
      Information need - is the topic about which the user wants to know more
      Query - is what we use to communicate to the computer to extract the information about the required topic
      
    Precision: What fraction of the returned results are relevant to the information need?
    Recall: What fraction of the relevant documents in the collection were returned by the system?
    
    Inverted Index : If you have to build a Incidence matrix for 500,000 documents then you are screwed cause you will get a sparse                           matrix , so to avoid this you build a inverted index where you have terms and an array of documents in which the                         term exists.
    
    Intersection based Algorithms : Based on binary operations 
    
    If the lists are too big then perform intersection using term frequency or do intersection just based on order of posting list and terms(using BigOh(N))
    
    INTERSECT(ht1, . . . , tni)
    terms ← SORTBYINCREASINGFREQUENCY(ht1, . . . , tni)
    result ← postings( f irst(terms))
    terms ← rest(terms)
    while terms 6= NIL and result 6= NIL
    do result ← INTERSECT(result, postings( f irst(terms)))
                terms ← rest(terms)
    return result
    
    INTERSECT(p1, p2)
    answer ← h i
    while p1 6= NIL and p2 6= NIL
    do if docID(p1) = docID(p2)
       then ADD(answer, docID(p1))
    p1 ← next(p1)
    p2 ← next(p2)
    else if docID(p1) < docID(p2)
            then p1 ← next(p1)
    else p2 ← next(p2)
    return answer
        
