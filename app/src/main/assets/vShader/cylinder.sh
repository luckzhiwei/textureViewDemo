attribute vec4 aColor;
attribute vec4 vPosition;
varying vec4 vColor;
uniform mat4 vMatrix;
void main()
{
 gl_Position =vMatrix* vPosition;
if(vPosition.z!=0.0){
        vColor=vec4(0.0,0.0,0.0,1.0);
    }else{
        vColor=vec4(0.99,0.99,0.99,1.0);
    }
}