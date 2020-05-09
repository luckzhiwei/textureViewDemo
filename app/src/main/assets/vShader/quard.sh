attribute vec4 aColor;
attribute vec4 vPosition;
varying vec4 vColor;
void main()
{
  gl_Position = vPosition;
  vColor = aColor;
}