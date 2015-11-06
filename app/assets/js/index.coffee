$ ->
  $.get "/companies", (companies) ->
    $.each companies, (index, company) ->
      $("#companies").append $("<li>").text company.name