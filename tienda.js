var LoginResponse;
var usuario;

/*desde la pagina donde se inicia sesión, se llama para iniciar la sesion de cualquier tipo
de usuario.*/
function login(){
    let password = document.getElementById("password").value;
    let nombreUsuario = document.getElementById("nombreUsuario").value;
    usuario = nombreUsuario;
    var url = 'http://localhost:8080/auth/login';
    var data = {
      "nombreUsuario": nombreUsuario,
      "password": password
    }
    fetch(url, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => res.json())
      .catch(error => console.error('Error:', error))
      .then(response => LoginResponse = response);
}

/*desde la página que se realiza el registro, el cual por medio de esta función 
se llenan los campos necesarios para el registro de un usuario normal. se debera llamar
cuando se pulse el botón para finalizar el registro.*/
function register(){
  let nombre = document.getElementById("nombre").value;
  let usuario = document.getElementById("usuario").value;
  let email = document.getElementById("email").value;
  let contrase = document.getElementById("contraseña").value;
  let numero = document.getElementById("numero").value;
  let calle = document.getElementById("calle").value;
  let colonia = document.getElementById("colonia").value;
  let ciudad = document.getElementById("ciudad").value;
  let estado = document.getElementById("estado").value;
  let pais = document.getElementById("pais").value;

  var url = 'http://localhost:8080/auth/nuevo';
    var data = {
      "nombre": nombre,
      "nombreUsuario": usuario,
      "email": email,
      "password": contrase,
      "telefono": numero,
      "direccion": calle + colonia + ciudad + estado + pais,
      "roles": ["user"]
  }
    fetch(url, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => res.json())
      .catch(error => console.error('Error:', error))
      .then(response => console.log('Response:', response));
}

/*se debera de llamar cada que acceda al catalogo, ya que aqui trae toda la información
desde la base de datos y hara que aparezca dínamicamentre.*/ 
function list(){
  var url = 'http://localhost:8080/auth/ObtenerBolsas';
  fetch(url, {
    method: 'GET',
    //body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(res => res.json())
  .catch(error => console.error('Error:', error))
  .then(response => console.log(response));
}

/*esta es una funcion es unicamente para el uso de un administrador y solo se llama
en caso de querer agregar un nuevo producto (Bolsa)*/
function addBolsa(){
  let modelo  = document.getElementById("modelo").value;
  let precio = document.getElementById("precio").value;
  let imagen = document.getElementById("imagen").value;
  let cantidad = document.getElementById("cantidad").value;
  let descripcion = document.getElementById("descripcion").value;

  var url = 'http://localhost:8080/Bolsa/agregaBolsa';
    var data =    {
      "modelo":modelo,
      "precio": precio,
      "imagen":imagen,
      "cantidad": cantidad,
      "descripcion":descripcion
  }

  fetch(url, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json',
      'Authorization' : 'Bearer ' + LoginResponse['token']
    }
  }).then(res => res.json())
  .catch(error => console.error('Error:', error))
  .then(response => console.log(response));
}

/*esta es una funcion es unicamente para el uso de un administrador y solo se llama
cuando se requiere quitar un producto del stock, solo se envia el modelo*/
function deleteBolsa(){
  let modelo = document.getElementById("modelo").value;
  var url = 'http://localhost:8080/Bolsa/borraBolsa';
    var data =    {
      "modelo":""+modelo
  }
  //console.log(LoginResponse["token"]);
  fetch(url, {
    method: 'DELETE',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json',
      'Authorization' : 'Bearer ' + LoginResponse['token']
    }
  }).then(res => res.json())
  .catch(error => console.error('Error:', error))
  .then(response => console.log(response));
}

/*esta es una funcion es unicamente para el uso de un administrador y solo se llama
cuando se requiere actualizar un producto*/
function PutBolsa(){
  let modelo  = document.getElementById("modelo").value;
  let precio = document.getElementById("precio").value;
  let imagen = document.getElementById("imagen").value;
  let cantidad = document.getElementById("cantidad").value;
  let descripcion = document.getElementById("descripcion").value;

  var url = 'http://localhost:8080/Bolsa/modificaBolsa';
    var data =    {
      "modelo":modelo,
      "precio": precio,
      "imagen":imagen,
      "cantidad": cantidad,
      "descripcion":descripcion
  }

  fetch(url, {
    method: 'PUT',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json',
      'Authorization' : 'Bearer ' + LoginResponse['token']
    }
  }).then(res => res.json())
  .catch(error => console.error('Error:', error))
  .then(response => console.log(response));
}

/*optiene un modelo en especifico*/
function GetModelo(){
  let modelo = document.getElementById("modelo").value;
  var url = 'http://localhost:8080/auth/Modelo';
    var data =    {
      "modelo":modelo
  }
  fetch(url, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(res => res.json())
  .catch(error => console.error('Error:', error))
  .then(response => console.log(response));
}

/**/
function pedido(){
  let bolsas;
  let montoTotal;
  var data = {
    "userName": usuario,
    "bolsa": bolsas,
    "montoTotal": montoTotal
}
var url = 'http://localhost:8080/Pedido/hacerPedido';
fetch(url, {
  method: 'POST',
  body: JSON.stringify(data),
  headers: {
    'Content-Type': 'application/json',
    'Authorization' : 'Bearer ' + LoginResponse['token']
  }
}).then(res => res.json())
.catch(error => console.error('Error:', error))
.then(response => console.log(response));

}

function  uploadFile() {
  var archivo = document.getElementById("file").files[0];  
  var formdata = new FormData();
  formdata.append("file", archivo);
  
  var requestOptions = {
    method: 'POST',
    body: formdata,
    redirect: 'follow'
  };
  
  fetch("http://localhost:8080/auth/upload", requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error', error));
}
