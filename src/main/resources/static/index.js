let chatbox = document.getElementById("chatbox");

document.getElementById("query-form").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent form submission

    let selectedDb = document.getElementById("database-select").value;
    let userQuery = document.getElementById("query-input").value;

    document.getElementById("query-input").value = "";

    let userDiv = document.createElement("div");
    userDiv.className = "message user";
    userDiv.innerHTML = "<strong>You:</strong> " + userQuery;
    chatbox.appendChild(userDiv);

    fetch("/api/ai", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            dbName: selectedDb,
            nlQuery: userQuery,
            schema: "public",
        }),
    })
        .then((response) => response.json())
        .then((data) => {
            let botMessage = data.sql;
            let botDiv = document.createElement("div");
            botDiv.className = "message bot";
            botDiv.innerHTML = "<strong>OpenAI tutor:</strong> " + marked.parse(botMessage);
            chatbox.appendChild(botDiv);
            chatbox.scrollTop = chatbox.scrollHeight;
        });
});
