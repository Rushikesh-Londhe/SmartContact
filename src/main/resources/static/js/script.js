
const toggleSidebar=()=>{
	
	if($(".sidebar").is(":visible")){
	//close
	$(".sidebar").css("display","none")	;
	$(".content").css("margin-left","0%");
	}
	else{
		//open
		$(".sidebar").css("display","block");	
	$(".content").css("margin-left","20%");
	}
		
	
};

const search = () => {
    let query = $("#search-input").val().trim();

    if (query === "") {
        $(".search-result").hide();
    } else {
        console.log(query);

        // Send a request to the server
        let url = `http://localhost:8088/search/${query}`;

        fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then((data) => {
                // Handle the response data
             //   console.log(data);
                
                let text=`<div class='list-group'>`
                
                data.forEach((contact)=>{
                
                text+=`<a href='/user/contact/${contact.cid}' class='list-group-item list-group-item-action'>${contact.name}</a>`
                });
                
                text+=`</div>`
                $(".search-result").html(text);
            })
            .catch((error) => {
                // Handle errors, e.g., display an error message
                console.error(error);
            });

        $(".search-result").show();
    }
};

//first request to server to create order
const paymentStart=()=>{
	console.log("started");
	
	let amount=$("#payment_field").val();
	console.log(amount);
	
	if(amount==""||amount==null){
		
		alert("amount is required!!");
		return;
	}
	
	//code
	//we wiil use ajax to sent req to server to create order-jequery
	
	$.ajax({
		url:'/user/create_order',
		data:JSON.stringify({amount:amount,info:'order request'}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response){
			//invoked when success
			console.log(response)
		},
		error:function(error){
			//invoked when error
			console.log(error)
			alert("something went wrong!!")
			
			
		}
		
	})
};
