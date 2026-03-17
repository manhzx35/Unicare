async function testRoles() {
  try {
    console.log("-> 1. Logging in as admin10@fpt.edu.vn...");
    const loginRes = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: 'admin10@fpt.edu.vn', password: 'password123' })
    });
    
    const data = await loginRes.json();
    if (!loginRes.ok) throw new Error(data.message || 'Login Failed');
    
    const token = data.accessToken || data.token;
    const adminId = data.id;
    console.log("Logged in! Token received. Default Role:", data.role);

    console.log("-> 2. Logging in as studentfpt@fpt.edu.vn to get its ID...");
    const studentLoginRes = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: 'studentfpt@fpt.edu.vn', password: 'password123' })
    });
    
    const studentData = await studentLoginRes.json();
    if (!studentLoginRes.ok) throw new Error(studentData.message || 'Student Login Failed');
    
    const studentId = studentData.id;
    console.log("Student ID is:", studentId);
    console.log("Student Default Role:", studentData.role);
    
    console.log("\n[TEST COMPLETED] User authentication works correctly.");
  } catch (error) {
    console.error("Error:", error.message);
  }
}

testRoles();
