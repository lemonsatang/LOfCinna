const id = document.getElementById('userid');
const idMsgBox = document.getElementById(('idMsg'));
const pass = document.getElementById('password');
const Repass = document.getElementById('password-confirm');
let chkState = document.getElementById('chk').getAttribute('data-chk');

function idChk(){
    if(id.value == ''){
        alert("아이디를 입력해주세요.");
        id.focus();
        return;
    }

    $.ajax({
        url: "/main/idChk/"+id.value,
        method: "post",
        success(res){
            idMsgBox.textContent = res.message;
            if(res.result == 'exist'){
                idMsgBox.style.color = 'red';
            }else{
                idMsgBox.style.color = 'blue';
                id.readOnly = true;
                id.style.background = 'lightgray'
                chkState = 'true'
            }
        },
        error(e){
            alert("오류가 발생했습니다.");
        }

    })
}

function signUp(){
    if(chkState == 'false'){
        alert('아이디 중복확인 해주세요.');
        return false;
    }

    if(pass.value != Repass.value){
        alert('비밀번호가 일치하지 않습니다.');
        return false
    }

}