#version 300 es

in vec4 a_position;
in vec4 a_color;
in vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 rounding;

out vec4 v_color;
out vec2 v_texCoords;

void main()
{
    v_color = a_color;
	v_color.a = v_color.a * (255.0/254.0);
	v_texCoords = a_texCoord0;

	vec4 temp = u_projTrans * a_position;
	temp.y = (floor(temp.y / rounding.y) + 0.5f) * rounding.y;
	temp.z = (floor(temp.z / rounding.x) + 0.5f) * rounding.x;
    //temp.y += 0.5f * rounding.y
    //temp.z += 0.5f * rounding.x
	gl_Position = temp;
}