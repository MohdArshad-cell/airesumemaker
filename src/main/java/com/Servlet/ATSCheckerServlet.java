package com.Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/checkATS")
public class ATSCheckerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // We no longer need an API key when using Ollama locally
        String resumeContent = request.getParameter("resumeContent");
        String jobDescription = request.getParameter("jobDescription");

        int atsScore = 0;
        String aiFeedback = "An error occurred. Make sure Ollama is running on your computer.";
        List<String> missingKeywords = new ArrayList<>();

        try {
            // The URL now points to the local Ollama server
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            String prompt = String.format("""
                You are an expert Applicant Tracking System (ATS). Analyze the resume against the job description.
                Respond ONLY with a valid JSON object in the specified format, without any surrounding text or markdown.
                The JSON format MUST be:
                {
                  "atsScore": <integer score from 0 to 100>,
                  "feedback": "<A few sentences of constructive feedback...>",
                  "missingKeywords": ["keyword1", "keyword2"]
                }
                Resume:
                %s
                Job Description:
                %s
                """, resumeContent, jobDescription);
            
            // Build the JSON payload for the Ollama API
            JSONObject payload = new JSONObject();
            payload.put("model", "llama2"); // The model we downloaded
            payload.put("prompt", prompt);
            payload.put("stream", false); // We want the full response at once
            payload.put("format", "json");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line);
                }
                reader.close();

                // Parse the Ollama response structure
                JSONObject jsonResponse = new JSONObject(responseBody.toString());
                // The AI's text is in the "response" field
                String aiJsonText = jsonResponse.getString("response");
                
                // Find the start of the JSON object in the response
                int jsonStartIndex = aiJsonText.indexOf("{");
                if(jsonStartIndex != -1) {
                    aiJsonText = aiJsonText.substring(jsonStartIndex);
                    JSONObject atsJson = new JSONObject(aiJsonText.trim());
                    atsScore = atsJson.optInt("atsScore", 0);
                    aiFeedback = atsJson.optString("feedback", "AI feedback could not be parsed.");
                    JSONArray keywordsJson = atsJson.optJSONArray("missingKeywords");
                    if (keywordsJson != null) {
                        for (int i = 0; i < keywordsJson.length(); i++) {
                            missingKeywords.add(keywordsJson.getString(i));
                        }
                    }
                } else {
                    aiFeedback = "Could not find valid JSON in the AI's response.";
                }
            } else {
                aiFeedback = "API Error: Received status code " + status;
            }

        } catch (Exception e) {
            e.printStackTrace();
            aiFeedback = "A Java error occurred: " + e.getMessage();
        }

      //...
        request.setAttribute("atsScore", atsScore);
        request.setAttribute("aiFeedback", aiFeedback);
        request.setAttribute("missingKeywords", missingKeywords);

        // --- ADD THESE TWO LINES ---
        request.setAttribute("submittedResumeContent", resumeContent);
        request.setAttribute("submittedJobDescription", jobDescription);
        // -------------------------

        RequestDispatcher dispatcher = request.getRequestDispatcher("atschecker.jsp");
        dispatcher.forward(request, response);
        //...
    }
}