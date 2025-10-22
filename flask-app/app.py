from flask import Flask, render_template
import random

app = Flask(__name__)

# Liste de gifs de chats
images = [
    "https://c.tenor.com/GTcT7HODLRgAAAAM/smiling-cat-creepy-cat.gif",
    "https://media0.giphy.com/media/10dU7AN7xsi1I4/giphy.webp",
    "https://media3.giphy.com/media/JIX9t2j0ZTN9S/200w.webp"
]

@app.route('/')
def index():
    url = random.choice(images)
    return render_template('index.html', url=url)

if __name__ == "__main__":
    app.run(host="0.0.0.0")
