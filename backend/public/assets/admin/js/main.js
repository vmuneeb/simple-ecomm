
$(document).ready(function() {

   $(".add-btn").click(function(){
        $(".add-card").toggle(500);
   })

    $('#category-select').on('change', function(){
    var selected_category = $(this).val();
            var $el = $("#sub-category-select");
            $el.find('option')
                .remove();
    $.each(subCjson, function(key,value) {
            if(value.category.id == selected_category) {
            $el.append($("<option></option>")
                                      .attr("value", value.id).text(value.name));
            }
        })
    })

    //prevent multiple submission
    $("form").submit(function() {
        $(this).submit(function() {
            return false;
        });
        return true;
    });


    //auto close alert
    $(".alert").delay(4000).slideUp(200, function() {
        $(this).alert('close');
    });

    $(".delete-brand-btn").click(function() {
    var id = $(this).data("id")
    deleteResource("/admin/products/brands/"+id);
    });

    $(".delete-category-btn").click(function() {
                   var id = $(this).data("id")
                   deleteResource("/admin/products/categories/"+id);
                   });
    $(".delete-subcategory-btn").click(function() {
                   var id = $(this).data("id")
                   deleteResource("/admin/products/subcategories/"+id);
                   });
    $(".delete-product-btn").click(function() {
        var id = $(this).data("id")
        deleteResource("/admin/products/"+id);
        });
    $(".delete-banner-btn").click(function() {
        var id = $(this).data("id")
        deleteResource("/admin/banner/"+id);
        });
     $(".cart_quantity_delete").click(function(){
         var id = $(this).data("id")
          deleteResource("/api/cart/"+id);
          deleteResource("/api/cart/"+id);
     })

     $(document)
     .on('click', 'form button[type=submit]', function(e) {
         var isValid = $(e.target).parents('form').isValid();
         if(!isValid) {
           e.preventDefault(); //prevent the default action
         }
     });



     $("#load-more-btn").click(function(){
                 var pn = $(this).data('page')
                 var search = $(this).data('search')
                 $.ajax({
                      type: "GET",
                      url: "/admin/orders?p="+pn+"&search="+search,
                      data: $("#address-form").serialize(), // serializes the form's elements.
                      success: function(data)
                      {
                        appendOrder(data);
                         $("#load-more-btn").data('page', pn+1);
                      },
                      error: function(data)
                      {
                         $("#load-more-btn").html("No more orders to show").attr("disabled",true);
                      }
                    });

           function appendOrder(orders) {
                 orders.forEach(function(order) {
                         $("#order-table tbody").append(getOrderDiv(order));
                 });
                 if(orders.length <= 0) {
                     $("#load-more-btn").html("No more orders to show").attr("disabled",true);
                 }
           }
     })

          $("#load-more-product-btn").click(function(){
                      var pn = $(this).data('page')
                      var search = $(this).data('search')
                      $.ajax({
                           type: "GET",
                           url: "/api/products?p="+pn+"&search="+search,
                           data: $("#address-form").serialize(), // serializes the form's elements.
                           success: function(data)
                           {
                              appendProduct(data);
                              $("#load-more-product-btn").data('page', pn+1);
                           },
                           error: function(data)
                           {
                              $("#load-more-product-btn").html("No more orders to show").attr("disabled",true);
                           }
                         });

                function appendProduct(products) {
                      products = products['records'];
                      products.forEach(function(product) {
                              // console.log(product);
                              $("#product-table tbody").append(getProductDiv(product));
                      });
                      if(products.length <= 0) {
                          $("#load-more-product-btn").html("No more products to show").attr("disabled",true);
                      }
                }
          })
})



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
        alert("successfully deleted")
    });

    // Callback handler that will be called on failure
    request.fail(function (jqXHR, textStatus, errorThrown){
        // Log the error to the console
        console.error(
            "The following error occurred: "+
            textStatus, errorThrown
        );
    });

    // Callback handler that will be called regardless
    // if the request failed or succeeded
    request.always(function () {
        // Reenable the inputs
        $("#spinner").removeClass("spinner");
        location.reload();
    });

}



function showModel(title,message) {
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
 $("#btn-close-modal").click(function(){
    window.location="/portal";
 })
}


function getOrderDiv(order) {
    var div  = "<tr class='"+order.status+"'> "+
                                            "<td>"+order.id+"</td>"+
                                            "<td>"+order.name+"</td>"+
                                            "<td>"+order.email+"</td>"+
                                            "<td>"+order.total_formatted+"</td>"+
                                            "<td>"+order.status+"</td>"+
                                            "<td>"+order.date_created+"</td>"+
                                            "<td><a class='btn btn-default btn-block' href='/admin/orders/"+order.id+"'>View</a></td>"+
                                       "</tr>";
    return div;
}

function getProductDiv(product) {
    var div  = "<tr > "+
                                            "<td>"+product.id+"</td>"+
                                            "<td><img height='60px' width='60px' src="+product.thumpNailImage+"></td>"+
                                            "<td>"+product.name+"</td>"+
                                            "<td>"+product.brand.name+"</td>"+
                                            "<td>"+product.price+"</td>"+
                                            "<td>"+product.discount_price+"</td>"+
                                            "<td>"+product.quantity+"</td>"+
                                            "<td><a  class='btn btn-danger delete-product-btn' data-id="+product.id+" style='margin-left: 20px' >Delete Product</a></td>"+
                                            "<td><a href='/admin/products/"+product.id+"' class='btn btn-default btn-success' data-id="+product.id+" >View</a></td>"+
                                       "</tr>";
    return div;
}