import React, { useState } from 'react';
import axios from 'axios';



function DocumentUploadForm(){
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const [statusMessage, setStatusMessage] = useState('');
    const [isUploaded, setIsUploaded] = useState(false);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
        setStatusMessage('');
    };

    const handleUploadProcess = async (e) => {
        e.preventDefault();
        if(!file){
            alert("Please select document!");
            return;
        }

        try{
            setLoading(true);
            setStatusMessage("Requesting secure upload link...");

            // Call Pre Signed URL service 
            const initResponse = await axios.post("http://localhost:8080/api/documents/signed-url", {
                fileName: file.name,
                contentType: file.type,
                fileSize: file.size
            });

            const uploadUrl = initResponse.data.uploadUrl;

            setStatusMessage("Uploading file directly to storage (MinIO/S3...");

            //upload file directly via PUT (Backend bypass raw binary)
            await axios.put(uploadUrl, file,{
                headers:{
                    'Content-Type': file.type
                }
            });

            setStatusMessage("Confirming upload with backend...");

            //upload confirm update status COMPLETED
            await axios.post(`http://localhost:8080/api/documents/confirm/${file.name}`);

            setStatusMessage("Success! Document uploaded and status updated to COMPLETED.");

        }catch(error){
            console.error("upload error:", error);
            const errMsg = error.response?.data?.message || error.message;
            setStatusMessage(`upload Failed: ${errMsg}`);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <h2>KYC / KYB Document Upload</h2>
            <form onSubmit={handleUploadProcess}>
                <div>
                    <label>Select Document (PDF, JPG, PNG - Max 5MB)</label>
                    <input type="file" onChange={handleFileChange}/>
                </div>
                <button
                type="submit"
                disabled={loading || !file || isUploaded}>
                    {loading ? "Processing..." : "Upload Securely"}
                </button>
            </form>
            <div>
                <p>{statusMessage}</p>
            </div>
        </div>
    )
}
export default DocumentUploadForm; 