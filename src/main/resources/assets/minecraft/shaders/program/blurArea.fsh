uniform sampler2D DiffuseSampler;
uniform vec2 BlurDir;
uniform float Radius;
uniform vec2 BlurXY;
uniform vec2 BlurCoord;

varying vec2 texCoord;

float roundedBox(vec2 frag, vec2 pos, vec2 size, float radius) {
    vec2 diff = abs(frag - (pos + size * 0.5)) - (size * 0.5 - vec2(radius));
    float d = length(max(diff, 0.0));
    return clamp(1.0 - smoothstep(radius - 1.0, radius + 1.0, d), 0.0, 1.0);
}

void main() {
    vec2 fragCoord = gl_FragCoord.xy;

    float mask = roundedBox(fragCoord, BlurXY, BlurCoord, 6.0); // raio 6px para bordas arredondadas

    if (mask <= 0.01) {
        gl_FragColor = texture2D(DiffuseSampler, texCoord);
        return;
    }

    vec4 sum = vec4(0.0);
    float total = 0.0;

    for (float t = -Radius; t <= Radius; t++) {
        vec2 offset = texCoord + BlurDir * t / 100.0;
        float weight = Radius - abs(t);
        sum += texture2D(DiffuseSampler, offset) * weight;
        total += weight;
    }

    gl_FragColor = mix(texture2D(DiffuseSampler, texCoord), sum / total, mask);
}