// XPath Grammar
//(absolute path) ap → doc(fileName)/rp
//                      | doc(fileName)//rp
//(relative path) rp → tagName | ∗ | . | .. | text() | @attName
//                      | (rp) | rp1/rp2 | rp1//rp2 | rp[f] | rp1, rp2
//(path filter) f → rp | rp1 = rp2 | rp1 eq rp2 | rp1 == rp2 | rp1 is rp2
//                      | (f) | f1 and f2 | f1 or f2 | not f

grammar XQuery;

// Define rules for absolute path
ap:

// Define rules for relative path
rp:

// Define rules for path filter
f: rp
    | rp '=' rp
    | rp 'eq' rp
    | rp '==' rp
    | rp 'is' rp
    | '(' f ')'
    | f 'and' f
    | f 'or' f
    | 'not' f
    ;

// Define tagName, fileName and attName
tagName: [A-Za-z0-9_\-]+;

fileName: [A-Za-z0-9/._]*;

attName: [A-Za-z0-9/._]*;
