This is a program which solves the problem of finding routes in a rail network. It reads sample static data of rail network from a file named StationMap.csv which is one level above this directory. If you wanna provide new data to the network, you are expected to overwrite this file's data.

It has two part:

1> Find out all the routes and print them in increasing no of stations travelled. 

Expected input: Travel from Holland Village to Bugis
Expected output: 
Stations travelled: 8
Route: ('CC21', 'CC20', 'CC19', 'DT9', 'DT10', 'DT11', 'DT12', 'DT13', 'DT14')

Take CC line from Holland Village to Farrer Road
Take CC line from Farrer Road to Botanic Gardens
Change from CC line to DT line
Take DT line from Botanic Gardens to Stevens
Take DT line from Stevens to Newton
Take DT line from Newton to Little India
Take DT line from Little India to Rochor
Take DT line from Rochor to Bugis

2> Find out all the routes in increasing order of travel time provided one start time and depending on conditions given (some station might take time or be non operational at the time the train reaches to that station).

Expected input: Travel from Holland Village to Bugis starting 2019-01-31 16:00:00 (format is yyyy-MM-dd HH:mm:ss).
Expected output: 
Time: 5280 seconds
Stations travelled: 8
Route: (CC21, CC20, CC19, DT9, DT10, DT11, DT12, DT13, DT14)

Take CC line from Holland Village to Farrer Road
Take CC line from Farrer Road to Botanic Gardens
Change from CC line to DT line
Take DT line from Botanic Gardens to Stevens
Take DT line from Stevens to Newton
Take DT line from Newton to Little India
Take DT line from Little India to Rochor
Take DT line from Rochor to Bugis


Time: 7800 seconds
Stations travelled: 11
Route: (CC21, CC22, EW21, EW20, EW19, EW18, EW17, EW16, EW15, EW14, EW13, EW12)

Take CC line from Holland Village to Buona Vista
Change from CC line to EW line
Take EW line from Buona Vista to Commonwealth
Take EW line from Commonwealth to Queenstown
Take EW line from Queenstown to Redhill
Take EW line from Redhill to Tiong Bahru
Take EW line from Tiong Bahru to Outram Park
Take EW line from Outram Park to Tanjong Pagar
Take EW line from Tanjong Pagar to Raffles Place
Take EW line from Raffles Place to City Hall
Take EW line from City Hall to Bugis

Instructions to build and run:
1> It has one setup file which can be run in any linux version. 
Copy Zandesk folder to any linux machine or linux docker image.
Go to bin folder run command : 
sh setup 
2>Run the program by running transit_system bash file by using command :
 sh transit_system 
3> After that you can provide input in the specified format and it will print the expected output in console.