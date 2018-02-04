// XPath Grammar
//(absolute path) ap → doc(fileName)/rp
//                      | doc(fileName)//rp
//(relative path) rp → tagName | ∗ | . | .. | text() | @attName
//                      | (rp) | rp1/rp2 | rp1//rp2 | rp[f] | rp1, rp2
//(path filter) f → rp | rp1 = rp2 | rp1 eq rp2 | rp1 == rp2 | rp1 is rp2
//                      | (f) | f1 and f2 | f1 or f2 | not f

grammar XQuery;

// Absolute Path
ap
    : 'doc(' fileName ')' '/' rp
    | 'doc(' fileName ')' '//' rp
    ;

// Relative Path
rp
    : tagName
    | '*'
    | '.'
    | '..'
    | 'text()'
    | '@' attName
    | '(' rp ')'
    | rp '/' rp
    | rp '//' rp
    | rp '[' f ']'
    | rp ',' rp
    ;

// Path Filter
f
    : rp
    | rp '=' rp
    | rp 'eq' rp
    | rp '==' rp
    | rp 'is' rp
    | '(' f ')'
    | f 'and' f
    | f 'or' f
    | 'not' f
    ;

fileName
    : PATHSTRING
    ;

tagName
    : IDSTRING
    ;

attName
    : IDSTRING
    ;

PATHSTRING
    : LetterOrDigit+
    ;

IDSTRING
    : LetterOrDigit+
    ;
    
fragment
Letter
    : [a-zA-Z_]
    ;

fragment
LetterOrDigit
    : [a-zA-Z0-9_-]
    ;
