// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;


import com.google.sps.data.Comment;
import com.google.sps.data.CommentsList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private CommentsList comments = new CommentsList(); 

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
    //reading GET request max-comments parameter to determine how much comments include in the response
    String commentsAction = getParameter(request, "comments", "current");
    String newestStr = getParameter(request, "newest", "");
    String oldestStr = getParameter(request, "oldest", "");
    String jsonArray = "";
    System.out.println(newestStr);
    System.out.println(oldestStr);
    long oldest = Long.MAX_VALUE;
    long newest = 0;
    try{
      oldest = Long.parseLong(oldestStr);
      newest = Long.parseLong(newestStr);
    }catch(NumberFormatException e){
      System.out.println("Cant parse");
    }

    switch(commentsAction){
      case "next":
        jsonArray = comments.nextChunk(oldest);
      break;
      case "prev":
        jsonArray = comments.prevChunk(newest);
      break;
      case "newest":
        jsonArray = comments.newestChunk();
      break;
    }

    response.setContentType("application/json;");
    response.getWriter().println(jsonArray);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //reading parameters from POST request
      String userName = getParameter(request, "user-name", "anonym");
      String comment = getParameter(request, "comment", "empty");

      //constructing comment object and saving in comments
      Comment commentObj = new Comment(userName, comment);
      comments.add(commentObj);

      //redirecting back to index.html
      // Redirect back to the HTML page.
      response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
