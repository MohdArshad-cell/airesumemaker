package com.Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/generate")
public class GenerateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // -------------------------
    // LATEX RESUME TEMPLATE SECTION
    // -------------------------
    // Paste your full LaTeX skeleton here between the triple quotes
    private static final String LATEX_TEMPLATE = """

\\documentclass[letterpaper,9pt]{article}
\\\\usepackage[empty]{fullpage}
\\\\usepackage{titlesec}
\\\\usepackage[hidelinks]{hyperref}
\\\\usepackage{enumitem}
\\\\usepackage{fancyhdr}
\\\\usepackage{tabularx}

\\\\pagestyle{fancy}
\\\\fancyhf{}
\\\\renewcommand{\\\\headrulewidth}{0pt}
\\\\renewcommand{\\\\footrulewidth}{0pt}

% Margins
\\\\addtolength{\\\\oddsidemargin}{-0.5in}
\\\\addtolength{\\\\textwidth}{1in}
\\\\addtolength{\\\\topmargin}{-0.7in}
\\\\addtolength{\\\\textheight}{1.35in}

\\\\urlstyle{same}
\\\\setlength{\\\\tabcolsep}{0in}

\\\\titleformat{\\\\section}{\\\\scshape\\\\large}{}{0em}{}[\\\\titlerule]

\\\\newcommand{\\\\resumeItem}[1]{\\\\item\\\\small{#1}}
\\\\newcommand{\\\\resumeSubheading}[4]{
  \\\\item
    \\\\begin{tabular*}{0.97\\\\textwidth}[t]{l@{\\\\extracolsep{\\\\fill}}r}
      \\\\textbf{#1} & #2 \\\\
      \\\\textit{\\\\small#3} & \\\\textit{\\\\small#4}
    \\\\end{tabular*}\\\\vspace{-6pt}
}
\\\\newcommand{\\\\resumeItemListStart}{\\\\begin{itemize}[leftmargin=*]}
\\\\newcommand{\\\\resumeItemListEnd}{\\\\end{itemize}\\\\vspace{-5pt}}

\\\\begin{document}

\\\\begin{center}
    \\\\textbf{\\\\Huge Jake Ryan} \\\\ \\\\vspace{1pt}
    \\\\small 123-456-7890 $|$ \\\\href{mailto:jake@su.edu}{jake@su.edu} $|$ 
    \\\\href{https://linkedin.com/in/jake}{linkedin.com/in/jake}
\\\\end{center}

\\\\section{Education}
\\\\begin{itemize}[leftmargin=*]
  \\\\resumeSubheading
    {Southwestern University}{Georgetown, TX}
    {B.A. Computer Science}{2018 -- 2021}
\\\\end{itemize}

\\\\section{Experience}
\\\\begin{itemize}[leftmargin=*]
  \\\\resumeSubheading
    {Research Assistant}{2020 -- Present}
    {Texas A\\\\&M University}{College Station, TX}
    \\\\resumeItemListStart
      \\\\resumeItem{Built REST API with FastAPI \\\\& PostgreSQL}
      \\\\resumeItem{Developed full-stack app using Flask \\\\& React}
    \\\\resumeItemListEnd
\\\\end{itemize}

\\\\section{Projects}
\\\\begin{itemize}[leftmargin=*]
  \\\\resumeItem{\\\\textbf{Gitlytics} — Full-stack GitHub analytics tool (Flask, React, Docker)}
\\\\end{itemize}

\\\\section{Skills}
\\\\begin{itemize}[leftmargin=*]
  \\\\item \\\\small \\\\textbf{Languages:} Java, Python, SQL
  \\\\item \\\\small \\\\textbf{Tools:} Git, Docker
\\\\end{itemize}

\\\\end{document}


""";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiKey = request.getParameter("apiKey");
        String resumeContent = request.getParameter("resumeContent");
        String jobDescription = request.getParameter("jobDescription");

        try {
            // The single, combined prompt for the Gemini API
            String finalPrompt =
                "You are an expert technical recruiter and professional resume writer specializing in LaTeX and ATS optimization.\n" +
                "Your task is to perform the following steps in order:\n" +
                "1.  **Analyze the Job Description** below to identify the most critical skills, keywords, and qualifications.\n" +
                "2.  **Rewrite and Optimize** the Original Resume to align perfectly with the requirements you identified. Integrate keywords naturally, rephrase bullet points to show impact, and reorder sections for relevance.\n" +
                "3.  **Format** the entire rewritten resume using the exact LaTeX skeleton provided.\n" +
                "4.  **Provide an ATS Evaluation** of the new resume you created.\n\n" +
                "**Inputs:**\n" +
                "--------------------------------\n" +
                "**Original Resume (in LaTeX):**\n" + resumeContent + "\n" +
                "--------------------------------\n" +
                "**Job Description:**\n" + jobDescription + "\n" +
                "--------------------------------\n" +
                "**LaTeX Skeleton (Use this exact structure):**\n" + LATEX_TEMPLATE + "\n" +
                "--------------------------------\n\n" +
                "**RESPONSE FORMAT:**\n" +
                "Provide your response in two parts separated by the exact delimiter `[---ATS---]`.\n" +
                "**Part 1:** The complete, optimized, and finalized LaTeX code for the resume.\n" +
                "**Part 2:** The ATS evaluation, formatted exactly as:\n" +
                "SCORE: [score from 1-10]\n" +
                "FEEDBACK: [A detailed analysis of the new resume's strengths and weaknesses.]";

            // A single API call
            String geminiOutput = callGeminiAPI(apiKey, finalPrompt);

            // Parse the combined output
            String optimizedResume = "Error: Could not parse AI response.";
            String atsEvaluation = "";
            String[] parts = geminiOutput.split("\\[---ATS---\\]");
            if (parts.length == 2) {
                optimizedResume = parts[0].trim();
                atsEvaluation = parts[1].trim();
            }

            // Combine results for the final output to the JSP
            String finalOutput = optimizedResume + "\n\n" + atsEvaluation;
            request.setAttribute("latexResult", finalOutput);
            request.getRequestDispatcher("result.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("latexResult", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("result.jsp").forward(request, response);
        }
    }

    private String callGeminiAPI(String apiKey, String prompt) throws IOException {
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]}]}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() != 200) {
            throw new IOException("API Error: " + conn.getResponseCode());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseBody.append(line.trim());
            }
            String output = responseBody.toString();
            int start = output.indexOf("\"text\": \"");
            if (start != -1) {
                output = output.substring(start + 9);
                int end = output.indexOf("\"");
                output = output.substring(0, end);
            }
            return output.replace("\\n", "\n");
        }
    }
}
