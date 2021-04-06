//Validação de campo numérico
//https://www.ime.usp.br/~kellyrb/mac2166_2015/tabela_ascii.html
function isNumberKey (evt){
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	
	if((charCode >= 48 && charCode <= 57) || charCode <=31){
		return true
	}
	
	return false; 
}