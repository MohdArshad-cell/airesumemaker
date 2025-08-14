<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.User" %>

<%-- Security Check --%>
<%
    if (session.getAttribute("loggedInUser") == null) {
        session.setAttribute("errorMsg", "Please login to continue.");
        response.sendRedirect("login.jsp");
        return; 
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>AI Cover Letter Writer - AI Resume Maker</title>
    <%@include file="all_components/head.jsp" %>
    <style>
        .workspace-textarea {
            height: 250px;
            font-size: 0.9rem;
        }
        .output-panel {
            background: rgba(0,0,0,0.3);
            border-radius: 8px;
            padding: 1.5rem;
            height: 100%;
            min-height: 500px;
            white-space: pre-wrap; /* Preserves formatting of the output */
            font-family: monospace;
        }
        .password-toggle {
            position: absolute;
            top: 50%;
            right: 15px;
            transform: translateY(-50%);
            cursor: pointer;
            color: var(--text-muted);
        }
    </style>
</head>
<body>
    <div class="background-container">
        <div class="aurora aurora-1"></div>
        <div class="aurora aurora-2"></div>
    </div>

    <main>
        <%@include file="all_components/navbar.jsp" %>

        <div class="container-fluid p-4">
            <div class="row g-4">
                
                <div class="col-lg-6">
                    <div class="content-card h-100">
                        <h4 class="mb-4"><i class="fas fa-file-signature me-2"></i>AI Cover Letter Generator</h4>
                        <form action="generateCoverLetter" method="post" id="coverletter-form">
                            <div class="mb-3">
                                <label for="resumeContent" class="form-label fw-bold">1. Your Resume Content:</label>
                                <textarea class="form-control workspace-textarea" id="resumeContent" name="resumeContent" placeholder="Paste your resume content here so the AI knows your skills and experience..."></textarea>
                            </div>
                             <div class="mb-3">
                                <label for="jobDescription" class="form-label fw-bold">2. Target Job Description:</label>
                                <textarea class="form-control workspace-textarea" id="jobDescription" name="jobDescription" placeholder="Paste the job description here..."></textarea>
                            </div>
                        
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-futuristic btn-lg" id="generate-btn">
                                    <span class="btn-text"><i class="fas fa-magic me-2"></i>Generate Letter</span>
                                    <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="content-card h-100">
                        <h4 class="mb-4"><i class="fas fa-robot me-2"></i>Generated Output</h4>
                        <div id="output-panel" class="output-panel text-muted">
                            Your AI-generated cover letter will appear here...
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>

    <%@include file="all_components/footer.jsp" %>

    <script>
document.addEventListener('DOMContentLoaded', function() {
    const coverLetterForm = document.getElementById('coverletter-form');
    const generateBtn = document.getElementById('generate-btn');
    const outputPanel = document.getElementById('output-panel');

    coverLetterForm.addEventListener('submit', function(event) {
        // Prevent the default form submission which causes a page reload
        event.preventDefault();

        // --- Show loading state ---
        const btnText = generateBtn.querySelector('.btn-text');
        const spinner = generateBtn.querySelector('.spinner-border');
        generateBtn.disabled = true;
        btnText.classList.add('d-none');
        spinner.classList.remove('d-none');
        // Add a temporary loading text span if it doesn't exist
        if (!generateBtn.querySelector('.loading-text')) {
             generateBtn.insertAdjacentHTML('beforeend', '<span class="loading-text ms-2">Generating...</span>');
        }
        outputPanel.textContent = 'The AI is thinking... Please wait.';
        outputPanel.classList.remove('text-muted');

        // --- Send data using AJAX (Fetch API) ---
        const formData = new FormData(coverLetterForm);
        
        fetch('generateCoverLetter', {
            method: 'POST',
            body: new URLSearchParams(formData)
        })
        .then(response => response.text())
        .then(text => {
            // Success: Update the output panel with the AI's response
            outputPanel.textContent = text;
        })
        .catch(error => {
            // Error: Show an error message
            console.error('Error:', error);
            outputPanel.textContent = 'An error occurred while communicating with the server.';
        })
        .finally(() => {
            // --- Reset button state regardless of success or error ---
            generateBtn.disabled = false;
            btnText.classList.remove('d-none');
            spinner.classList.add('d-none');
            const loadingText = generateBtn.querySelector('.loading-text');
            if(loadingText) {
                loadingText.remove();
            }
        });
    });
    
    // This part is for the API key visibility toggle, you can remove it if you remove the API key field
    const toggleApiKey = document.getElementById('toggleApiKey');
    const apiKeyInput = document.getElementById('apiKey');
    if (toggleApiKey) {
        toggleApiKey.addEventListener('click', function() {
            const type = apiKeyInput.getAttribute('type') === 'password' ? 'text' : 'password';
            apiKeyInput.setAttribute('type', type);
            this.classList.toggle('fa-eye');
            this.classList.toggle('fa-eye-slash');
        });
    }
});
</script>
</body>
</html>