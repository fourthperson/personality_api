# Simple Personality Test API

Simple Personality-Test REST API for the [Personality Test App](https://github.com/fourthperson/personality_test_app)
built on Java8, [Spark](https://sparkjava.com/) Web
Framework, [ORM Lite](https://ormlite.com/), [Moshi](https://github.com/square/moshi),
and [MariaDB](https://mariadb.org/)

## Introduction

The API receives and returns only JSON <code>application/json</code>.<br/><br/>
Each response JSON body has a <code>status</code> key which is an integer and a <code>data</code> key which contains the
data pertaining to that response.<br/><br/>
Status value <code>200</code> means Success, with the requested information in the <code>data</code> key.<br/>
Status value <code>500</code> means that an error occurred, and it will return a string
description of the error under the <code>data</code> key.

## Demo

API is hosted at https://perstestapi.iaklabs.ink as the base URL

## Routes

### Questions (GET)

<pre><code>/questions</code></pre>

Used to fetch a list of simple personality-test questions.

#### Request

Send a standard GET request to the <code>/questions</code> endpoint with no payload or body data.

#### Response

Returns JSON with an array of 20 questions under the <code>data</code> key

<pre>
<code>
{
    "status": 200,
    "data": [
        {
            "id": 1,
            "text": "I prefer one-on-one conversations to group activities",
            "created_on": "2022-09-26 13:58:06"
        },
        {
            "id": 2,
            "text": "I often prefer to express myself in writing",
            "created_on": "2022-09-26 13:58:19"
        },
        {
            "id": 3,
            "text": "I seem to care about wealth, fame, and status less than my peers",
            "created_on": "2022-09-26 13:58:37"
        }
    ]
}
</code>
</pre>

### Evaluate (POST)

<pre><code>/evaluate</code></pre>

Used to evaluate the user's answers. Accepts input in JSON format. Content-Type has to be <code>application/json</code>
when calling this request with a JSON body.<br/>

#### Request

The incoming JSON Body for the request should be as follows:
<pre>
<code>
{
    "answer_count": 5,
    "answers": "true;false;true;true;true"
}
</code>
</pre>
where <code>answer_count</code> is the number of questions that the user answered, and <code>answers</code> is a
concatenated string of "true" and "false" answers to the questions, separated by a semicolon<code>;</code>.<br/>
Any spaces in the <code>answers</code> string will be removed.

#### Response

Returns the result of the analysis.
<pre>
<code>
{
    "status": 200,
    "data": "Introverted"
}
</code>
</pre>
