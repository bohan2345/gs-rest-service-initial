def fact(n)
 puts 'Logs happens, some string in console'
 if n==0
    return 1
 else
    return n*fact(n-1)
 end
end