//<![CDATA[
		
		/**
		
		 */
		function highlightRides(date) {
			var dayOfMonth = date.getDate();

		
			if (typeof data-available-days !== 'undefined' && data-available-days.includes(dayOfMonth)) {
			
				return [true, 'ride-available', 'Egun honetan bidaiak badira'];
			}

			
			return [false, '', ''];
		}
		
		//]]>