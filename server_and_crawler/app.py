from flask import Flask
from flask_restful import Resource, Api
from Retrieval import Retrieval

app = Flask(__name__)
api = Api(app)


class GetEvents(Resource):
    def get(self, query_string):
        retrival = Retrieval()
        return retrival.query(query_string)

api.add_resource(GetEvents, '/<string:query_string>')

if __name__ == '__main__':
    #app.run(debug=True)
    app.run(host='0.0.0.0')
    # Run this to be seen from the outside world app.run(host='0.0.0.0')
