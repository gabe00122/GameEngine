uniform sampler2D u_texture0;

in vec2 v_texCoords;

void main(){
    gl_FragColor = texture(u_texture0, v_texCoords);
}