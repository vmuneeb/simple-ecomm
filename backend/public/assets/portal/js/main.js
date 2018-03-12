/*scroll to top*/

$(document).ready(function(){
	$(function () {
		$.scrollUp({
	        scrollName: 'scrollUp', // Element ID
	        scrollDistance: 300, // Distance from top/bottom before showing element (px)
	        scrollFrom: 'top', // 'top' or 'bottom'
	        scrollSpeed: 300, // Speed back to top (ms)
	        easingType: 'linear', // Scroll to top easing (see http://easings.net/)
	        animation: 'fade', // Fade, slide, none
	        animationSpeed: 200, // Animation in speed (ms)
	        scrollTrigger: false, // Set a custom triggering element. Can be an HTML string or jQuery object
					//scrollTarget: false, // Set a custom target element for scrolling to the top
	        scrollText: '<i class="fa fa-angle-up"></i>', // Text for element, can contain HTML
	        scrollTitle: false, // Set a custom <a> title if required.
	        scrollImg: false, // Set true to use image
	        activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
	        zIndex: 2147483647 // Z-Index for the overlay
		});
	});
});


$("#load-more-btn").click(function(){
            var pn = $(this).data('page')
            var param = $(this).data('param');
            var param_value = $(this).data('value')
            $.ajax({
                 type: "GET",
                 url: "/api/products?p="+pn+"&"+param+"?"+param_value,
                 success: function(data)
                 {
                    appendProducts(data['records']);
                    $("#load-more-btn").data('data-page', pn+1);
                 },
                 error: function(data)
                 {
                    $("#load-more-btn").remove();
                 }
               });

      function appendProducts(products) {
            products.forEach(function(product) {
                    $(".features_items").append(getProductDiv(product));
            });
            if(products.length < 20) {
                $("#load-more-btn").html("No more product to show").attr("disabled",true);
            }
      }
})



$(document).ready(function( ) {
    $('.add-cart-btn').click(function(e) {
       productId = $(this).data("id");
       addToCart(productId);
     });
//     $("#checkout-btn").click(function(){
//        checkout();
//     })

     $(".category-expand").click(function(){
         $(this).parent().next('ul').slideToggle();
     });

      $(".cart_quantity_delete").click(function(ev){
        var id = $(this).data("id")
         $.ajax({
                   type: "DELETE",
                   url: "/api/cart/"+id,
                   data: $("#address-form").serialize(), // serializes the form's elements.
                   success: function(data)
                    {
                       window.location.reload();
                    },
                   error: function(data)
                    {
                       showModel("Error",data.responseText,false);
                     }
                    });
          })

     $("#btn-create-order").click(function(ev){
       $.ajax({
                 type: "POST",
                 url: "/api/orders",
                 data: $("#address-form").serialize(), // serializes the form's elements.
                 success: function(data)
                 {
                     showModel("Success","Order created successfully",true);
                 },
                 error: function(data)
                 {
                    showModel("Error",data.responseText,false);
                 }
               });
     })
})




function addToCart(productId) {
  if(!loggedIn) {
    showLoginModel();
    return;
  }
  $("#spinner").addClass("spinner");
  data  = {"product_variant_id" : productId,"quantity": $("#qunatity").val()};
  request = $.ajax({
        url: "/api/cart",
        type: "POST",
        dataType: "json",
        data: data
    });

    // Callback handler that will be called on success
    request.done(function (response, textStatus, jqXHR){
        // Log a message to the console
        updateCart(response);
    });

    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        // Log the error to the console
        showModel("Error",jqXHR.responseText,false);
    });

    // Callback handler that will be called regardless
    // if the request failed or succeeded
    request.always(function () {
        // Reenable the inputs
        $("#spinner").removeClass("spinner");
    });

}


function updateCart(cart) {
 $("#cart-count").html(cart.product_count);
 $("#add-cart-notification").removeClass("hidden");
}



function deleteResource(url) {

    $("#spinner").addClass("spinner");

    request = $.ajax({
        url: url,
        type: "DELETE",
        dataType: "json"
    });

    // Callback handler that will be called on success
    request.done(function (response, textStatus, jqXHR){
        // Log a message to the console
        window.location.reload();
    });

    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        // Log the error to the console

    });

    // Callback handler that will be called regardless
    // if the request failed or succeeded
    request.always(function () {
        // Reenable the inputs
        $("#spinner").removeClass("spinner");
        location.reload();
    });


}


function showModel(title,message,gotOrders) {
var model = $("<div class='modal fade' id='alert-modal' tabindex='-1' role='dialog'> "+
                               "<div class='modal-dialog' role='document'> "+
                                 "<div class='modal-content'> "+
                                   "<div class='modal-header'> "+
                                     "<button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button> "+
                                     "<h4 class='modal-title'>"+title+"</h4> "+
                                   "</div> "+
                                   "<div class='modal-body'> "+
                                     "<p>"+message+"</p> "+
                                   "</div> "+
                                   "<div class='modal-footer'> "+
                                     "<button type='button' class='btn btn-default' id='btn-close-modal' data-dismiss='modal'>Ok</button> "+
                                   "</div>"+
                                 "</div>"+
                               "</div>"+
                             "</div>");
 $(".modal").remove();
 $('body').append(model);
 $('#alert-modal').modal('show');
 if(gotOrders){
  $("#btn-close-modal").click(function(){
     window.location="/orders";
  })
 }
}


function showLoginModel(title,message) {
var model = $("<div class='modal fade' id='alert-modal' tabindex='-1' role='dialog'> "+
                               "<div class='modal-dialog' role='document'> "+
                                 "<div class='modal-content  col-md-8 col-md-offset-2'> "+
                                   "<div class='modal-body'> "+
                                                            "<div class='text-center'>"+
                                                                    "<h3><i class='fa fa-users fa-4x'></i></h3>"+
                                                                    "<h2 class='text-center'>Login</h2>"+
                                                                    "<div class='panel-body'>"+
                                                                        "<form class='form' method='post' action='/login'>"+
                                                                            "<fieldset>"+
                                                                                "<div class='form-group'>"+
                                                                                   "<div class='input-group'>"+
                                                                                        "<span class='input-group-addon'><i class='fa fa-user color-blue'></i></span>"+
                                                                                        "<input name='email' placeholder='email address' class='form-control' type='email' oninvalid='setCustomValidity('Please enter a valid email address!')' onchange='try{setCustomValidity('')}catch(e){}' required=''>"+
                                                                                    "</div>"+
                                                                                "</div>"+
                                                                                "<div class='form-group'>"+
                                                                                    "<div class='input-group'>"+
                                                                                        "<span class='input-group-addon'><i class='fa fa-lock color-blue'></i></span>"+
                                                                                        "<input name='password' placeholder='password' class='form-control' type='password'  required=''>"+
                                                                                    "</div>"+
                                                                                "</div>"+
                                                                                "<div class='form-group'>"+
                                                                                    "<input class='btn btn-lg btn-primary btn-block' value='Log In' type='submit'>"+
                                                                                "</div>"+
                                                                                "<div class='form-group'><a class='pull-left' href='/forgot' class='pull-right'>Forgot password?</a><a class='pull-right' href='/signup'>create account</a></span></div>"+
                                                                            "</fieldset>"+
                                                                        "</form>"+
                                                                   "</div>"+
                                                            "</div>"+
                                   "</div> "+
                                 "</div>"+
                               "</div>"+
                             "</div>");
 $(".modal").remove();
 $('body').append(model);
 $('#alert-modal').modal('show');
}


function getProductDiv(product) {
var div = "<div class='col-md-3 col-lg-3'> "+
           								"<div class='product-image-wrapper'>"+
           									"<a href='/product/"+product.id+"'>"+
           										"<div class='single-products'>"+
           											"<div class='productinfo text-center'>"+
           												"<img class='img-responsive' src='"+product.thumpNailImage+"' alt='' />"+
//           												"@if(product.price > product.discountPrice) {"+
//           												"<strike class='discount-price'>Price: @product.getPrice() AED</strike>"+
//           												"} else{"+
//                                                        "   <p>&nbsp;</p>"+
//           												"}"+
           												"<h2>"+product.discount_price+"AED+</h2>"+
           												"<p>"+product.name+"+</p>"+
           											"</div>"+
           										"</div>"+
           									"</a>"+
           								"</div>"+
           							"</div>";
    return div;
}