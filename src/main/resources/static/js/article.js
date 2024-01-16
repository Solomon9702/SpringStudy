// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if(deleteButton){
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        });
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if(modifyButton){
    modifyButton.addEventListener('click', event =>{
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`,{
            method: 'PUT',
            headers: {
                "Content-Type" : "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
        .then(() => {
            alert('수정이 완료되었습니다.');
            location.replace(`/articles/${id}`);
        });
    });
}


// 등록 가능
const createButton = document.getElementById("create-btn")
if(createButton){
    createButton.addEventListener("click", (event) =>{
        fetch("/api/articles",{
            method: "POST",
            headers:{
                "Content-Type":"application/json",
            },
            body: JSON.stringify({
                title:document.getElementById("title").value,
                content:document.getElementById("content").value,
            }),
        }).then(()=>{
            location.replace("/articles");
        })
    })
}


// PDF변환 파일기능
document.getElementById('convert-pdf-btn').addEventListener('click', () => {
    // PDF로 변환할 HTML 요소 선택
    const element = document.body; // 혹은 특정 요소를 선택할 수 있습니다.

    // html2pdf 옵션 설정
    const opt = {
        margin:       10,
        filename:     'myfile.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { scale: 2 },
        jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };

    // HTML 요소를 PDF로 변환
    html2pdf().from(element).set(opt).save();
});

