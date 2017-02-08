program 
  var N as int ; 
  var SQRT as int ; 
begin 
  N := readint ; 
  SQRT := 0 ; 

  while SQRT * SQRT <= N do 
    SQRT := SQRT + 1 ; 
  end ; 

  SQRT := SQRT - 1 ;

  writeint SQRT ; 

end 
