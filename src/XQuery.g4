

grammar XQuery;

// XQuery
//    XQ → V ar | StringConstant | ap
//    | (XQ1) | XQ1, XQ2 | XQ1/rp| XQ1//rp
//    | htagNamei{XQ1}h/tagNamei
//    | forClause letClause whereClause returnClause
//    | letClause XQ1
//    forClause → for V ar1 in XQ1, V ar2 in XQ2, . . . , V arn in XQn
//    letClause →  | let V arn+1 := XQn+1, . . . , V arn+k := XQn+k
//    whereClause →  | where Cond
//    returnClause → return XQ1
//    Cond → XQ1 = XQ2 | XQ1 eq XQ2
//    | XQ1 == XQ2 | XQ1 is XQ2
//    | empty(XQ1)
//    | some V ar1 in XQ1, . . . , V arn in XQn satisfies Cond
//    | (Cond1) | Cond1 and Cond2 | Cond1 or Cond2 | not Cond1
query: xq EOF?;

xq
    : var                                               #xq_var
    | '<' tagName '>' '{' xq '}' '<' '/' tagName '>'    #xq_makeElement
    | strConstant                                       #xq_makeText
    | ap                                                #xq_ap
    | '(' xq ')'                                        #xq_xq
    | xq ',' xq                                         #xq_concat
    | xq '/' rp                                         #xq_nextLevel
    | xq '/' '/' rp                                     #xq_nextLevelRecursive
    | forClause letClause? whereClause? returnClause    #xq_loop
    | letClause xq                                      #xq_let
    ;

var : '$' IDSTRING;
strConstant: STRING | PATHSTRING;
forClause: 'for' var 'in' xq (',' var 'in' xq)*;
letClause: 'let' var ':=' xq (',' var ':=' xq)*;
whereClause: 'where' cond;
returnClause: 'return' xq;
cond
    : xq op=('='|'eq') xq                               #cond_equal
    | xq op=('=='|'is') xq                              #cond_is
    | 'empty' '(' xq ')'                                    #cond_empty
    | 'some' var 'in' xq (var 'in' xq)* 'satisfies' cond    #cond_some
    | '(' cond ')'                                          #cond_cond
    | cond 'and' cond                                       #cond_and
    | cond 'or' cond                                        #cond_or
    | 'not' cond                                            #cond_not
    ;

// Below is for XPath
// XPath Grammar
//    (absolute path) ap → doc(fileName)/rp
//                          | doc(fileName)//rp
//    (relative path) rp → tagName | ∗ | . | .. | text() | @attName
//                          | (rp) | rp1/rp2 | rp1//rp2 | rp[f] | rp1, rp2
//    (path filter) f → rp | rp1 = rp2 | rp1 eq rp2 | rp1 == rp2 | rp1 is rp2
//                          | (f) | f1 and f2 | f1 or f2 | not f
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


// lexer
PATHSTRING
    : '"' [A-Za-z0-9./_]* '"'
    ;

IDSTRING
    : [a-zA-Z_][a-zA-Z0-9\-_.]*
    ;

STRING
 :  '"' ~["]* '"'
 ;

SPACE
 : [ \n\t\r] -> skip
 ;

//COMMENT
// : '#' ~[\n\r]* -> skip
// ;

