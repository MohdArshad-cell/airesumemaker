package com.Servlet;
// Add all necessary imports...
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/getLatexCode") // Note the new URL
public class GetLatexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resumeContent = request.getParameter("resumeContent");
        String jobDescription = request.getParameter("jobDescription");
        String generatedText = "Error: Could not generate LaTeX code.";

        try {
            // This prompt is the same as before
            String prompt = createLatexPrompt(resumeContent, jobDescription);
            
            // Reusing the Ollama call logic
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            JSONObject payload = new JSONObject();
            payload.put("model", "llama2");
            payload.put("prompt", prompt);
            payload.put("stream", false);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                JSONObject jsonResponse = new JSONObject(sb.toString());
                generatedText = jsonResponse.getString("response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().print(generatedText);
    }

    private String createLatexPrompt(String resume, String jobDesc) {
         return String.format("""
            Act as an expert resume writer... Your output MUST be a complete and valid LaTeX document...
            ---
            USER'S RESUME:
            %s
            ---
            TARGET JOB DESCRIPTION:
            %s
            ---
            """, resume, jobDesc);
    }
}