
function alertMessage(info,message) {
    $(function(){
        $(".close").click(function(){
            $("#myAlert").alert();
        });
    });
    var cc2=document.getElementById("alert2");
    cc2.innerHTML="&times";
    var cc=document.getElementById("myAlert3");
    var cc3=document.getElementById("alertspan");
    cc3.innerHTML=message;
    if(info==='success'){
        cc.classList.add("alert-success");
    }
    else if(info==='failure'){
       cc.classList.add("alert-danger")
    }
    else{
       cc.classList.add("alert-warning")
    }
    var a=document.createElement("a");
/*    cc.appendChild(a);
    a.className="close";
    a.href="#";
    a.innerHTML="&times";
*/
}
