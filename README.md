# clojure-visualize
A visualization library that will hopefully bring to light a different perspective of your Clojure project.

# Setup
Right now, the set-up is not very easy because this was not a major feature during the Hackathon.

- Clone repository
- Open src/visualize/core.clj and change the project-path to the path of your desired project
- Run lein repl -> (-main)
- This generates a visualize.json at the [project-path].
- Copy the visualize.json and move it to web/ directory in your clojure-visualize directory.
- Start a simpleHTTPServer from the web/ directory using pythons SimpleHTTPServer.
- Navigate to http://127.0.0.1:8000/ or whatever your default server is located at.
- Enjoy!

# Future
Eventually I would like this to be a plugin library that with a lein compand will generate a visual report of your porject. This will include the current dependency graphic map, as well as, graphics detailing lines of code, number of files, heat map of most used code, redundency code, likliehood of errors, clojuricness, etc. The idea is to eventually make a visual report card for your project.
