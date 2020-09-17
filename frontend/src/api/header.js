export default function makeheader() {
    return {
      headers: {
        "Content-type": "application/json",
        "jwt-token": sessionStorage.getItem('jwt-token'),
        "Access-Control-Allow-Origin": "*"
      }
    }
  }