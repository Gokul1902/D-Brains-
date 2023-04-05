    
let home=document.querySelector('.home-inner');
let playground=document.querySelector('.inner-play');
let mySql=document.querySelector('.inner-sql');
let postgrey=document.querySelector('.inner-post');
let converter=document.querySelector('.inner-converter');
let communicate=document.querySelector('.inner-communicate');
let path=document.querySelector('.top-two')

function _(value){
    return document.getElementById(value);
}

function hello(){
    alert("hello");
}

function profileShower(){
    if(_("disp").style.opacity=="1"){
        _("clip").classList.remove("afterprofilecilp");
        _("disp").style.opacity="0";
        
    }
    else{
		_("clip").classList.add("afterprofilecilp");
        _("disp").style.opacity="1";
    }
}

function expandHome(){
    document.querySelector('span').style.transform="rotate()"
    if(home.offsetHeight=='0'){
        // path.innerHTML="Home"
        home.style.height="fit-content";
    }
    else{
        // path.innerHTML="Home "
        home.style.height='0'
    }   
}
function expandPlay(){
    
    if(playground.offsetHeight==0){
        // path.innerHTML="Home > playground"
        playground.style.height="fit-content";
    }
    else{
        playground.style.height='0'
        // path.innerHTML="Home"
    }  
    

}
function expandMysql(){
    // path.innerHTML="Home > playground > mysql"
    if(mySql.offsetHeight==0){
        mySql.style.height="fit-content";
        // path.innerHTML="Home > playground > mysql"
    }
    else{
        mySql.style.height='0'
        // path.innerHTML="Home > playground"
    }  
}

function expandPost(){
    
    if(postgrey.offsetHeight==0){
        // path.innerHTML="Home > playground > postgreysql"
        postgrey.style.height="fit-content";
    }
    else{
        postgrey.style.height='0'
        // path.innerHTML="Home > playground "
    }  
}

function expandConverter(){
    
    if(converter.offsetHeight==0){
        // path.innerHTML="Home > converter"
        converter.style.height="fit-content";
        // path.innerHTML="Home "
    }
    else{
        converter.style.height='0'
    }  
}
function expandCommunicate(){
    // path.innerHTML="Home > communication"
    if(communicate.offsetHeight==0){
        communicate.style.height="fit-content";
    }
    else{
        communicate.style.height='0'
        // path.innerHTML="Home"
    }  
}
function profile() {
    let cookies = document.cookie.split("=");
    if (cookies[1] == undefined || cookies[1] == '') {
        location.href = "loginSample.html";
    }

}

var x=10;
var level="";
function detailShow(){
    const x = new XMLHttpRequest();
    x.onreadystatechange = function(){
        if (x.readyState == 4 && x.status == 200){
        	  var jsonobj =JSON.parse(x.responseText);

        	  document.getElementById("loginname").innerHTML=jsonobj["loginname"];
        	  document.getElementById("loginmail").innerHTML=jsonobj["loginmail"];
        	  /*document.getElementById("circle").innerHTML=jsonobj["loginsingle"];*/
              document.getElementById("pro-l").innerText=jsonobj["loginsingle"];
              level=jsonobj["level"];
              profile();
            }

        }
    x.open("get","logdetail");
    x.send();
}
detailShow();

function StatusTeller(){
    if(level=="Server"){
        alert("This Machine has Server status. So, This machine has more priority.")
    }
    else{
        alert("This Machine has Client status. So, This machine has been restricted for some process.")
    }
}