attribute vec4 aColor;
attribute vec4 vPosition;
uniform mat4 vMatrix;
varying vec4 vColor;
void main()
{
gl_Position =vMatrix*vPosition;
vColor = aColor;
}