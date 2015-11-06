$ ->
  $.get "/addressReferenceTypes", (types) ->
    $.each types, (index, type) ->
      $("#types").append $("<option>").text type.name

$ ->
  $.get "/rootAddressReferences", (refs) ->
    $.each refs, (index, ref) ->
      $("#refs").append("<option value=\"#{ref._id}\">#{ref.text}</option>")