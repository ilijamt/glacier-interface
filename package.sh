#!/bin/bash

SCRIPT=`realpath -s $0`
WORK_DIR=`dirname $SCRIPT`
TMP_DIR=`mktemp -d`
DEB_FILE="${TMP_DIR}.deb"

die () {
    echo >&2 "$@"
    exit 1
}

[ "$#" -eq 2 ] || die "2 arguments required, $# provided, $0 <version> <pkg-name>"

PKG_VERSION="$1"
PKG_NAME="$2"
DEST_DEB_FILE="${WORK_DIR}/build/${PKG_NAME}-${PKG_VERSION}.deb"

if [ ! -f "$WORK_DIR/build/libs/$PKG_NAME.jar" ]; then
	echo "Cannot find main jar file"
	exit -1;
fi;

# Create a copy of the package folder 
cp -R $WORK_DIR/debian $TMP_DIR/DEBIAN

# Copy the built files to the file
mkdir -p "$TMP_DIR/usr/share/$PKG_NAME"
mkdir -p "$TMP_DIR/usr/share/doc/$PKG_NAME"
mkdir -p "$TMP_DIR/usr/bin"
mkdir -p "$TMP_DIR/etc/bash_completion.d/"
cp "$WORK_DIR/build/libs/$PKG_NAME.jar" "$TMP_DIR/usr/share/$PKG_NAME"
cp "$WORK_DIR/resources/gi" "$TMP_DIR/usr/bin"
#cp "$WORK_DIR/resources/bash-autocomplete" "$TMP_DIR/etc/bash_completion.d/gi"
gzip -9c "${WORK_DIR}/debian/changelog" > "${TMP_DIR}/usr/share/doc/${PKG_NAME}/changelog.gz"
cp "${WORK_DIR}/debian/copyright" "${TMP_DIR}/usr/share/doc/${PKG_NAME}/copyright"
chmod +x "$TMP_DIR/usr/bin/gi"
rm "$TMP_DIR/DEBIAN/changelog" "$TMP_DIR/DEBIAN/compat" "$TMP_DIR/DEBIAN/copyright"

PACKAGE_SIZE=`du -bs "$TMP_DIR" | cut -f 1`
PACKAGE_SIZE=$((PACKAGE_SIZE/1024))
echo "Installed-Size: $PACKAGE_SIZE" >> "$TMP_DIR/DEBIAN/control"
sed -i "s/%version%/$PKG_VERSION/g" "$TMP_DIR/DEBIAN/control"

fakeroot chown -R root "$TMP_DIR"
fakeroot chgrp -R root "$TMP_DIR"

# Package 
cd $TMP_DIR
md5sum "usr/share/$PKG_NAME/$PKG_NAME.jar" >> "$TMP_DIR/DEBIAN/md5sums"
md5sum "usr/share/doc/${PKG_NAME}/changelog.gz" >> "$TMP_DIR/DEBIAN/md5sums"
md5sum "usr/share/doc/${PKG_NAME}/copyright" >> "$TMP_DIR/DEBIAN/md5sums"
md5sum "usr/bin/gi" >> "$TMP_DIR/DEBIAN/md5sums"
#md5sum "etc/bash_completion.d/gi" >> "$TMP_DIR/DEBIAN/md5sums"
fakeroot dpkg-deb --build $TMP_DIR

# Clean 
rm -rf $TMP_DIR

mv "$DEB_FILE" "$DEST_DEB_FILE"

lintian -i "$DEST_DEB_FILE" > "$WORK_DIR/build/lint" 

rm $TMP_DIR -rf

exit 0