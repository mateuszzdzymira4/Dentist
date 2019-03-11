		    var nav = new DayPilot.Navigator("nav");
		    
		    nav.showMonths = 3;
		    nav.selectMode = "week";
		    nav.onTimeRangeSelected = function(args) {
		        dp.startDate = args.start;
		        
		        dp.update();
		    };
		    nav.init();
		    
		    var dp = new DayPilot.Calendar("dp");
		
		    // view
		    dp.viewType = "Week";
		    
		   
		    
		    
		    dp.init();
		    
 			var idString = "ID: "
			    
			    var theList = /*[[${visits}]]*/[];
			    for (i = 0; i < theList.length; i++) {
			    	
			    	
			    	///MS
			    	var x = theList[i].date+"T"+theList[i].hour+":00";
				    var y = new Date(x);
				    
				    var visittimeh = 1; //czas trwania wizyty w godzinach

				    var z = y.getFullYear()+"-"+("0" + (y.getMonth() + 1)).slice(-2)+
				    "-"+("0" + y.getDate()).slice(-2)+"T"+("0" + (y.getHours()+visittimeh)).slice(-2)+
				    ":"+("0" + (y.getMinutes())).slice(-2)+":00";
				    
				    var k = theList[i].hour+" "+theList[i].purpose;

				    
				   var e = new DayPilot.Event({
			            start: x,
			            end: z,
			            id: theList[i].id,
			            //theList[i].id,
			            text: theList[i].hour+" "+theList[i].patient.fname+" "+theList[i].patient.lname+" "+theList[i].purpose
			        });
			        
			      
				    dp.events.add(e);
			        
				    dp.onEventClick = function(args) {
				       //alert("clicked: " + args.e.id());
				       
				       window.location='visitinfo/'+args.e.id();
				    };
			        
			    	////MS
			    	
			    }
			    
		    
			    
		