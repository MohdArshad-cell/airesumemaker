package com.Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/generateCoverLetter")
public class GenerateCoverLetterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String resumeContent = request.getParameter("resumeContent");
        String jobDescription = request.getParameter("jobDescription");

        String generatedText = "Error: Could not generate cover letter. Please ensure Ollama is running.";

        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            // A new prompt specifically for writing cover letters
            String prompt = String.format("""
                Act as a professional career coach. Based on the provided resume and job description, write a compelling, 
                professional, and concise cover letter. The tone should be confident but not arrogant.
                The letter should highlight the most relevant skills and experiences from the resume that match the job description.
                Do not include placeholders like "[Your Name]" or "[Hiring Manager Name]". Start directly with the letter's body.

                ---
                USER'S RESUME:
                %s

                ---
                TARGET JOB DESCRIPTION:
                %s
                ---
                """, resumeContent, jobDescription);

            JSONObject payload = new JSONObject();
            payload.put("model", "llama2");
            payload.put("prompt", prompt);
            payload.put("stream", false);

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

                JSONObject jsonResponse = new JSONObject(responseBody.toString());
                generatedText = jsonResponse.getString("response");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the content type to plain text and send the response back to the JavaScript
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(generatedText);
        out.flush();
    }
}