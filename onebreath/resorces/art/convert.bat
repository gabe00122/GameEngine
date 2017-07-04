for %%f in (*.ase) do aseprite -b %%f --save-as "%%~nf.png"
pause