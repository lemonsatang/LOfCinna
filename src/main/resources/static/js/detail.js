const commentSubmit = document.getElementById("commentSubmit");
const commentText = document.getElementById("commentText");
const commentList = document.getElementById("commentList");
const bno = window.location.pathname.split("/")[3];

document.addEventListener("DOMContentLoaded", function(){
    getComment();
})

// 댓글 등록
commentSubmit.addEventListener("click", () => {
    const text = commentText.value.trim();
    if (text === "") return alert("댓글을 입력해주세요!");

    let obj = {
        bno: bno,
        content : text
    }

    $.ajax({
        url:'/board/addComment',
        method:'post',
        data:obj,
        success(res){
            alert(res.message);
            if(res.success){
                getComment();
                commentText.value = '';
            }
        },
        error(e){
            alert('오류가 발생했습니다.');
            return false;
        }
    })

});

// 댓글 가져오기, 댓글 삭제
function getComment(){
    commentList.innerHTML = '';
    $.ajax({
        url:'/board/getComment/'+bno,
        method: 'post',
        success(res) {
            for(let i=0;i<res.length;i++){
                let item = res[i];
                const commentItem = document.createElement("div");
                    commentItem.classList.add("comment-item");
                     commentItem.innerHTML = ``;
                    if(item.same){
                        commentItem.innerHTML += `<button class="comment-delete-btn">삭제</button>`
                    }
                    commentItem.innerHTML += `<p class="comment-content">${item.content}</p>
                                                <div class="comment-info">
                                                  <span class="comment-writer">${item.writer}</span>
                                                  <span class="comment-date">${new Date().toLocaleDateString()}</span>
                                                </div>
                
                                          `;
                if(item.same) {
                    commentItem.querySelector(".comment-delete-btn").addEventListener("click", (e) => {
                        console.log(item.cno)
                        if (confirm("정말 삭제하시겠습니까?")) {
                            $.ajax({
                                url:'/board/delComment/'+item.cno,
                                method:'post',
                                success(res){
                                    if(res.success){
                                        commentItem.remove();
                                    }
                                    alert(res.message);
                                },
                                error(e){
                                    alert('오류가 발생했습니다.')
                                }
                            })
                        }
                    });
                }

                commentList.prepend(commentItem);
            }
        },
        error(e) {
            return false;
        }
    })
}

// 글 삭제
function delBoard(bno,fileName){

    let obj = {
        bno : bno,
        fileName: fileName
    }

    if(confirm('정말 삭제하시겠습니까?')){
        $.ajax({
            url:'/board/delBoard',
            method:'post',
            data: obj,
            success(res){
                if(res.success){
                    alert('삭제 완료되었습니다.');
                    location.href = '/board/list';
                }else{
                    alert('삭제도중 문제가 발생했습니다.');
                    return;
                }
            },
            error(e){
                alert("오류가 발생했습니다.")
            }
        })
    }
}

