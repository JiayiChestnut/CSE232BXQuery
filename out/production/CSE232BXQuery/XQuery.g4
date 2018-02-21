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
    : 'doc' '(' fileName ')' '/' ? '/' rp
    ;

// Relative Path
rp
    : tagName               #rp_tagNmae
    | '*'                   #rp_children
    | '.'                   #rp_current
    | '..'                  #rp_parent
    | 'text' '(' ')'        #rp_text
    | '@' attName           #rp_attName
    | '(' rp ')'            #rp_rp
    | rp '/' rp             #rp_nextLevel
    | rp '/' '/' rp         #rp_nextLevelRecursive
    | rp '[' f ']'          #rp_filter
    | rp ',' rp             #rp_concat
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
    : '"' [A-Za-z0-9./_]* '"'
    ;

IDSTRING
    : [a-zA-Z0-9_-]+
    ;
