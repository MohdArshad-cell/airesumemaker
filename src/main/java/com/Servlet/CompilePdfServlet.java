package com.Servlet;
// Add all necessary imports...
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/compilePdf") // Note the new URL
public class CompilePdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String latexCode = request.getParameter("latexContent");
        Path tempDir = null;

        try {
            // This is the same compilation logic as before
            tempDir = Files.createTempDirectory("latex-compile-");
            String uniqueFileName = "resume-" + UUID.randomUUID().toString();
            Path texFilePath = tempDir.resolve(uniqueFileName + ".tex");
            Files.writeString(texFilePath, latexCode, StandardCharsets.UTF_8);

            ProcessBuilder pb = new ProcessBuilder("pdflatex", "-output-directory=" + tempDir.toString(), texFilePath.toString());
            Process process = pb.start();
            process.waitFor();

            Path pdfFilePath = tempDir.resolve(uniqueFileName + ".pdf");
            if (!Files.exists(pdfFilePath)) {
                throw new Exception("LaTeX compilation failed to produce a PDF.");
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"AI-Tailored-Resume.pdf\"");
            Files.copy(pdfFilePath, response.getOutputStream());
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        } finally {
            // Clean up temporary files
            if (tempDir != null) {
                try {
                    Files.walk(tempDir).map(Path::toFile).forEach(File::delete);
                    Files.deleteIfExists(tempDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}